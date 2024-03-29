/*

 * @(#)GapContent.java	1.3 98/04/09

 * 

 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.

 * 

 * This software is the confidential and proprietary information of Sun

 * Microsystems, Inc. ("Confidential Information").  You shall not

 * disclose such Confidential Information and shall use it only in

 * accordance with the terms of the license agreement you entered into

 * with Sun.

 * 

 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE

 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE

 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR

 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES

 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING

 * THIS SOFTWARE OR ITS DERIVATIVES.

 * 

 */



import java.util.Vector;
import java.io.Serializable;
import javax.swing.text.*;
import javax.swing.undo.UndoableEdit;
import javax.swing.SwingUtilities;



/**

 * An implementation of the AbstractDocument.Content interface 

 * implemented using a gapped buffer similar to that used by emacs.

 * The underlying storage is a array of unicode characters with

 * a gap somewhere.  The gap is moved to the location of changes

 * to take advantage of common behavior where most changes are

 * in the same location.  Changes that occur at a gap boundry are

 * generally cheap and moving the gap is generally cheaper than

 * moving the array contents directly to accomodate the change.

 * <p>

 * The positions tracking change are also generally cheap to

 * maintain.  The Position implementations (marks) store the array

 * index and can easily calculate the sequential position from

 * the current gap location.  Changes only require update to the

 * the marks between the old and new gap boundries when the gap

 * is moved, so generally updating the marks is pretty cheap.

 * The marks are stored sorted so they can be located quickly

 * with a binary search.  This increases the cost of adding a

 * mark, and decreases the cost of keeping the mark updated.

 *

 * @author  Timothy Prinzing

 * @version 1.3 04/09/98

 */

public final class GapContent implements AbstractDocument.Content, Serializable {





    /**

     * Creates a new GapContent object.  Initial size defaults to 10.

     */

    public GapContent() {

	this(10);

    }



    /**

     * Creates a new GapContent object, with the initial

     * size specified.

     *

     * @param initialLength the initial size

     */

    public GapContent(int initialLength) {

	array = new char[initialLength];

	array[0] = '\n';

	g0 = 1;

	g1 = initialLength;

    }



    // --- AbstractDocument.Content methods -------------------------



    /**

     * Returns the length of the content.

     *

     * @return the length >= 1

     * @see AbstractDocument.Content#length

     */

    public int length() {

	int len = array.length - (g1 - g0);

	return len;

    }



    /**

     * Inserts a string into the content.

     *

     * @param where the starting position >= 0, < length()

     * @param str the non-null string to insert

     * @return an UndoableEdit object for undoing

     * @exception BadLocationException if the specified position is invalid

     * @see AbstractDocument.Content#insertString

     */

    public UndoableEdit insertString(int where, String str) throws BadLocationException {

	if (where >= length()) {

	    throw new BadLocationException("Invalid insert", length());

	}

	char[] chars = str.toCharArray();

	replace(where, 0, chars);

	return null;

    }



    /**

     * Removes part of the content.

     *

     * @param where the starting position >= 0, where + nitems < length()

     * @param nitems the number of characters to remove >= 0

     * @return an UndoableEdit object for undoing

     * @exception BadLocationException if the specified position is invalid

     * @see AbstractDocument.Content#remove

     */

    public UndoableEdit remove(int where, int nitems) throws BadLocationException {

	if (where + nitems >= length()) {

	    throw new BadLocationException("Invalid insert", length() + 1);

	}

	replace(where, nitems, empty);

	return null;

	

    }



    /**

     * Retrieves a portion of the content.

     *

     * @param where the starting position >= 0

     * @param len the length to retrieve >= 0

     * @return a string representing the content

     * @exception BadLocationException if the specified position is invalid

     * @see AbstractDocument.Content#getString

     */

    public String getString(int where, int len) throws BadLocationException {

	Segment s = new Segment();

	getChars(where, len, s);

	return new String(s.array, s.offset, s.count);

    }



    /**

     * Retrieves a portion of the content.  If the desired content spans

     * the gap, we copy the content.  If the desired content does not

     * span the gap, the actual store is returned to avoid the copy since

     * it is contiguous.

     *

     * @param where the starting position >= 0, where + len <= length()

     * @param len the number of characters to retrieve >= 0

     * @param chars the Segment object to return the characters in

     * @exception BadLocationException if the specified position is invalid

     * @see AbstractDocument.Content#getChars

     */

    public void getChars(int where, int len, Segment chars) throws BadLocationException {

	if (where < 0) {

	    throw new BadLocationException("Invalid location", -1);

	}

	if ((where + len) > length()) {

	    throw new BadLocationException("Invalid location", length() + 1);

	}

	if ((where + len) <= g0) {

	    // below gap

	    chars.array = array;

	    chars.offset = where;

	} else if (where >= g0) {

	    // above gap

	    chars.array = array;

	    chars.offset = g1 + where - g0;

	} else {

	    // spans the gap, must copy

	    chars.array = new char[len];

	    chars.offset = 0;

	    int before = g0 - where;

	    System.arraycopy(array, where, chars.array, 0, before);

	    System.arraycopy(array, g1, chars.array, before, len - before);

	}

	chars.count = len;

    }



    /**

     * Creates a position within the content that will

     * track change as the content is mutated.

     *

     * @param offset the offset to track >= 0

     * @return the position

     * @exception BadLocationException if the specified position is invalid

     */

    public Position createPosition(int offset) throws BadLocationException {

	if (marks == null) {

	    marks = new Vector();

	    search = new MarkData(0);

	}

	if (unusedMarks > Math.max(5, (marks.size() / 10))) {

	    removeUnusedMarks();

	}

	int index = (offset < g0) ? offset : offset + (g1 - g0);

	MarkData m = new MarkData(index);

	int sortIndex = findSortIndex(m);

	marks.insertElementAt(m, sortIndex);

	return new StickyPosition(m);

    }



    /**

     * Holds the data for a mark... seperately from

     * the real mark so that the real mark (Position

     * that the caller of createPosition holds) can be 

     * collected if there are no more references to

     * it.  The update table holds only a reference

     * to this data.

     */

    final class MarkData {



	MarkData(int index) {

	    this.index = index;

	}



	/**

	 * Fetch the location in the contiguous sequence

	 * being modeled.  The index in the gap array

	 * is held by the mark, so it is adjusted according

	 * to it's relationship to the gap.

	 */

        public final int getOffset() {

	    int offs = (index < g0) ? index : index - (g1 - g0);

	    return Math.max(offs, 0);

	}



        public final void dispose() {

	    unused = true;

	    unusedMarks += 1;

	}



	int index;

	boolean unused;

    }



    /**

     * This really wants to be a weak reference but

     * in 1.1 we don't have a 100% pure solution for

     * this... so this class trys to hack a solution 

     * to causing the marks to be collected.

     */

    final class StickyPosition implements Position {



	StickyPosition(MarkData mark) {

	    this.mark = mark;

	}



        public final int getOffset() {

	    return mark.getOffset();

	}



	protected void finalize() throws Throwable {

	    // schedule the record to be removed later

	    // on another thread.

	    mark.dispose();

	}



        public String toString() {

	    return Integer.toString(getOffset());

	}



	MarkData mark;

    }



    // --- variables --------------------------------------



    private static final char[] empty = new char[0];

    private transient Vector marks;



    /**

     * Record used for searching for the place to

     * start updating mark indexs when the gap 

     * boundries are moved.

     */

    private transient MarkData search;



    /**

     * The number of unused mark entries

     */

    private transient int unusedMarks;



    /**

     * The array of unicode characters that store 

     * the content.

     */

    char[] array;



    /**

     * start of gap in the array

     */

    int g0;



    /**

     * end of gap in the array

     */

    int g1;





    // --- gap management -------------------------------



    /**

     * Replace the given logical position in the storage with

     * the given new items.  This will move the gap to the area

     * being changed if the gap is not currently located at the

     * change location.

     *

     * @param position the location to make the replacement.  This

     *  is not the location in the underlying storage array, but

     *  the location in the contiguous space being modeled.

     * @param rmSize the number of items to remove

     * @param addItems the new items to place in storage.

     */

    void replace(int position, int rmSize, char[] addItems) {

	int addSize = addItems.length;

	int addOffset = 0;

	if (addSize == 0) {

	    close(position, rmSize);

	    return;

	} else if (rmSize > addSize) {

	    /* Shrink the end. */

	    close(position+addSize, rmSize-addSize);

	} else {

	    /* Grow the end, do two chunks. */

	    int endSize = addSize - rmSize;

	    int end = open(position + rmSize, endSize);

	    System.arraycopy(addItems, rmSize, array, end, endSize);

	    addSize = rmSize;

	}

	System.arraycopy(addItems, addOffset, array, position, addSize);

    }



    /** 

     * Delete nItems at position.  Squeezes any marks 

     * within the deleted area to position.  This moves

     * the gap to the best place by minimizing it's 

     * overall movement.  The gap must intersect the

     * target block.

     */

    void close(int position, int nItems) {

	if (nItems == 0)  return;



	int end = position + nItems;

	int new_gs = (g1 - g0) + nItems;

	if (end <= g0) {

	    // Move gap to end of block.

	    if (g0 != end) {

		shiftGap(end);

	    }

	    // Adjust g0.

	    shiftGapStartDown(g0 - nItems);

	} else if (position >= g0) {

	    // Move gap to beginning of block.

	    if (g0 != position) {

		shiftGap(position);

	    }

	    // Adjust g1. 

	    shiftGapEndUp(g0 + new_gs);

	} else {

	    // The gap is properly inside the target block.

	    // No data movement necessary, simply move both gap pointers.

	    shiftGapStartDown(position);

	    shiftGapEndUp(g0 + new_gs);

	}

    }



    /**

     * Make space for the given number of items at the given

     * location.  

     *

     * @returns the location that the caller should fill in.

     */

    int open(int position, int nItems) {

	int gapSize = g1 - g0;

	if (nItems == 0) {

	    if (position > g0)  

		position += gapSize;

	    return position;

	}



	// Expand the array if the gap is too small.

	shiftGap(position);

	if (nItems >= gapSize) {

	    // Pre-shift the gap, to reduce total movement.

	    shiftEnd(array.length - gapSize + nItems);

	    gapSize = g1 - g0;

	}



	g0 = g0 + nItems;

	return position;

    }



    /** 

     * resize the underlying storage array to the 

     * given new size

     */

    void resize(int nsize) {

	char[] narray = new char[nsize];

	System.arraycopy(array, 0, narray, 0, Math.min(nsize, array.length));

	array = narray;

    }



    /**

     * Make the gap bigger, moving any necessary data and updating 

     * the appropriate marks

     */

    void shiftEnd(int newSize) {

	int oldSize = array.length;

	int oldGapEnd = g1;

	int upperSize = oldSize - oldGapEnd;

	int newGapEnd;

	long dg;



	if (newSize < oldSize) {

	    if (oldSize <= array.length) {

		// No more downsizing.

		return;

	    }

	    if (upperSize > 0) {

		/* When contracting, move vector contents to front. */

		shiftGap(oldSize - (g1 - g0));

		oldGapEnd = oldSize;

		upperSize = 0;

	    }

	}



	resize(newSize);

	newGapEnd = array.length - upperSize;

	g1 = newGapEnd;

	dg = newGapEnd - oldGapEnd;



	// Adjust marks.

	int adjustIndex = findMarkAdjustIndex(oldGapEnd);

	int n = marks.size();

	for (int i = adjustIndex; i < n; i++) {

	    MarkData mark = (MarkData) marks.elementAt(i);

	    mark.index += dg;

	}

	

	if (upperSize != 0) {

	    // Copy array items to new end of array.

	    System.arraycopy(array, oldGapEnd, array, newGapEnd, upperSize);

	}

    }



    /**

     * Move the start of the gap to a new location,

     * without changing the size of the gap.  This 

     * moves the data in the array and updates the

     * marks accordingly.

     */

    void shiftGap(int newGapStart) {

	if (newGapStart == g0) {

	    return;

	}

	int oldGapStart = g0;

	int dg = newGapStart - oldGapStart;

	int oldGapEnd = g1;

	int newGapEnd = oldGapEnd + dg;

	int gapSize = oldGapEnd - oldGapStart;



	g0 = newGapStart;

	g1 = newGapEnd;

	if (dg > 0) {

	    // Move gap up, move data and marks down.

	    int adjustIndex = findMarkAdjustIndex(oldGapStart);

	    int n = marks.size();

	    for (int i = adjustIndex; i < n; i++) {

		MarkData mark = (MarkData) marks.elementAt(i);

		if (mark.index >= newGapEnd) {

		    break;

		}

		mark.index -= gapSize;

	    }

	    System.arraycopy(array, oldGapEnd, array, oldGapStart, dg);

	} else if (dg < 0) {

	    // Move gap down, move data and marks up.

	    int adjustIndex = findMarkAdjustIndex(newGapStart);

	    int n = marks.size();

	    for (int i = adjustIndex; i < n; i++) {

		MarkData mark = (MarkData) marks.elementAt(i);

		if (mark.index >= oldGapEnd) {

		    break;

		}

		mark.index += gapSize;

	    }

	    System.arraycopy(array, newGapStart, array, newGapEnd, -dg);

	}

    }



    /**

     * Adjust the gap end downward.  This doesn't move

     * any data, but it does update any marks affected 

     * by the boundry change.  All marks from the old

     * gap start down to the new gap start are squeezed

     * to the end of the gap (their location has been

     * removed).

     */

    void shiftGapStartDown(int newGapStart) {

	// Push aside all marks from oldGapStart down to newGapStart.

	int adjustIndex = findMarkAdjustIndex(newGapStart);

	int n = marks.size();

	for (int i = adjustIndex; i < n; i++) {

	    MarkData mark = (MarkData) marks.elementAt(i);

	    if (mark.index > g0) {

		// no more marks to adjust

		break;

	    }

	    mark.index = g1;

	}

	g0 = newGapStart;

    }



    /**

     * Adjust the gap end upward.  This doesn't move

     * any data, but it does update any marks affected 

     * by the boundry change. All marks from the old

     * gap end up to the new gap end are squeezed

     * to the end of the gap (their location has been

     * removed).

     */

    void shiftGapEndUp(int newGapEnd) {

	int adjustIndex = findMarkAdjustIndex(g1);

	int n = marks.size();

	for (int i = adjustIndex; i < n; i++) {

	    MarkData mark = (MarkData) marks.elementAt(i);

	    if (mark.index >= newGapEnd) {

		break;

	    }

	    mark.index = newGapEnd;

	}

	g1 = newGapEnd;

    }



    /**

     * Compares two marks.

     *

     * @param o1 the first object

     * @param o2 the second object

     * @return < 0 if o1 < o2, 0 if the same, > 0 if o1 > o2

     */

    final int compare(MarkData o1, MarkData o2) {

	if (o1.index < o2.index) {

	    return -1;

	} else if (o1.index > o2.index) {

	    return 1;

	} else {

	    return 0;

	}

    }



    /**

     * Finds the index to start mark adjustments given

     * some search index.

     */

    final int findMarkAdjustIndex(int searchIndex) {

	search.index = Math.max(searchIndex, 1);

	int index = findSortIndex(search);



	// return the first in the series

	// (ie. there may be duplicates).

	for (int i = index - 1; i >= 0; i--) {

	    MarkData d = (MarkData) marks.elementAt(i);

	    if (d.index != search.index) {

		break;

	    }

	    index -= 1;

	}

	return index;

    }



    /**

     * Finds the index of where to insert a new mark.

     *

     * @param o the mark to insert

     * @return the index

     */

    final int findSortIndex(MarkData o) {

	int lower = 0; 

	int upper = marks.size() - 1;

	int mid = 0;

	

	if (upper == -1) {

	    return 0;

	}



	int cmp = 0;

	MarkData last = (MarkData) marks.elementAt(upper);

	cmp = compare(o, last);

	if (cmp > 0)

	    return upper + 1;

	

	while (lower <= upper) {

	    mid = lower + ((upper - lower) / 2);

	    MarkData entry = (MarkData) marks.elementAt(mid);

	    cmp = compare(o, entry);



	    if (cmp == 0) {

		// found a match

		return mid;

	    } else if (cmp < 0) {        

		upper = mid - 1;

	    } else {

		lower = mid + 1;

	    }

	}



	// didn't find it, but we indicate the index of where it would belong.

	return (cmp < 0) ? mid : mid + 1;

    }



    /**

     * Remove all unused marks out of the sorted collection

     * of marks.  

     */

    final void removeUnusedMarks() {

	int n = marks.size();

	Vector cleaned = new Vector(n);

	for (int i = 0; i < n; i++) {

	    MarkData mark = (MarkData) marks.elementAt(i);

	    if (mark.unused == false) {

		cleaned.addElement(mark);

	    }

	}

	marks = cleaned;

	unusedMarks = 0;

    }



}





