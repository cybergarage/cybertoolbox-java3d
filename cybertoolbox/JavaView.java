/******************************************************************
*
*	CyberToolbox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File : JavaView.java
*
******************************************************************/

import java.awt.*;
import java.util.Vector;

import javax.swing.text.*;

public class JavaView extends WrappedPlainView {

	private	JavaDocument.Scanner	lexer;
	private	boolean					lexerValid;
	private	JavaContext				javaContext;
	
	/**
	 * Construct a simple colorized view of java
	 * text.
	 */
	public JavaView(Element elem, JavaContext context) {
		super(elem);
		Debug.message("JavaView");
		JavaDocument doc = (JavaDocument) getDocument();
		lexer = doc.createScanner();
		lexerValid = false;
		javaContext = context;
		Debug.message("    JavaView.doc  = " + doc);
	}

	/**
	 * Renders using the given rendering surface and area 
	 * on that surface.  This is implemented to invalidate
	 * the lexical scanner after rendering so that the next
	 * request to drawUnselectedText will set a new range
	 * for the scanner.
	 *
	 * @param g the rendering surface to use
	 * @param a the allocated region to render into
	 *
	 * @see View#paint
	 */
	
	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
		lexerValid = false;
	}

	/**
	 * Renders the given range in the model as normal unselected
	 * text.  This is implemented to paint colors based upon the
	 * token-to-color translations.  To reduce the number of calls
	 * to the Graphics object, text is batched up until a color
	 * change is detected or the entire requested range has been
	 * reached.
	 *
	 * @param g the graphics context
	 * @param x the starting X coordinate
	 * @param y the starting Y coordinate
	 * @param p0 the beginning position in the model
	 * @param p1 the ending position in the model
	 * @returns the location of the end of the range
	 * @exception BadLocationException if the range is invalid
	 */
	 
	protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
		Document doc = getDocument();
		Color last = null;
		int mark = p0;
		for (; p0 < p1; ) {
			updateScanner(p0);
			int p = Math.min(lexer.getEndOffset(), p1);
			p = (p <= p0) ? p1 : p;
			Color fg = javaContext.getForeground(lexer.token);
			if (fg != last && last != null) {
				// color change, flush what we have
				g.setColor(last);
				Segment text = getLineBuffer();
				doc.getText(mark, p0 - mark, text);
				x = Utilities.drawTabbedText(text, x, y, g, this, mark);
				mark = p0;
			}
			last = fg;
			p0 = p;
		}
		
		// flush remaining
		g.setColor(last);
		Segment text = getLineBuffer();
		doc.getText(mark, p1 - mark, text);
		x = Utilities.drawTabbedText(text, x, y, g, this, mark);
		return x;
	}

	/**
	 * Update the scanner (if necessary) to point to the appropriate
	 * token for the given start position needed for rendering.
	 */
	public void updateScanner(int p) {
		try {
			if (! lexerValid) {
				JavaDocument doc = (JavaDocument) getDocument();
				lexer.setRange(doc.getScannerStart(p), doc.getLength());
				lexerValid = true;
			}
			while (lexer.getEndOffset() <= p) {
				lexer.scan();
			}
		} catch (Throwable e) {
			// can't adjust scanner... calling logic
			// will simply render the remaining text.
			e.printStackTrace();
		}
	}

	public int getTabSize() {
		int size = 4;
		//Debug.message("getTabSize");
		Document doc = getDocument();
		//Debug.message("    doc = " + doc);
		if (doc != null) {
			Integer i = (Integer) doc.getProperty(PlainDocument.tabSizeAttribute);
			if (i != null) {
				size = i.intValue();
			}
		}
		return size;
	}
		
}

