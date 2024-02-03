/*
 * @(#)JavaContext.java	1.2 98/05/04
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
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

import java.awt.*;
import java.util.Vector;

import javax.swing.text.*;

/**
 * A collection of styles used to render java text.  
 * This class also acts as a factory for the views used 
 * to represent the java documents.  Since the rendering 
 * styles are based upon view preferences, the views need
 * a way to gain access to the style settings which is 
 * facilitated by implementing the factory in the style 
 * storage.  Both functionalities can be widely shared across
 * java document views.
 *
 * @author   Timothy Prinzing
 * @version  1.2 05/04/98
 */
public class JavaContext extends StyleContext implements ViewFactory {

    /**
     * Constructs a set of styles to represent java lexical 
     * tokens.  By default there are no colors or fonts specified.
     */
    public JavaContext() {
	super();
	Style root = getStyle(DEFAULT_STYLE);
	tokenStyles = new Style[JavaToken.MaximumScanValue + 1];
	JavaToken[] tokens = JavaToken.all;
	int n = tokens.length;
	for (int i = 0; i < n; i++) {
	    JavaToken t = tokens[i];
	    Style parent = getStyle(t.getCategory());
	    if (parent == null) {
		parent = addStyle(t.getCategory(), root);
	    }
	    Style s = addStyle(null, parent);
	    s.addAttribute(JavaToken.TokenAttribute, t);
	    tokenStyles[t.getScanValue()] = s;

	}
    }

    /**
     * Fetch the foreground color to use for a lexical
     * token with the given value.
     * 
     * @param attr attribute set from a token element
     *  that has a JavaToken in the set.
     */
    public Color getForeground(int code) {
	if (tokenColors == null) {
	    tokenColors = new Color[JavaToken.MaximumScanValue + 1];
	}
	if ((code >= 0) && (code < tokenColors.length)) {
	    Color c = tokenColors[code];
	    if (c == null) {
		Style s = tokenStyles[code];
		c = StyleConstants.getForeground(s);
	    }
	    return c;
	}
	return Color.black;
    }

    /**
     * Fetch the font to use for a lexical
     * token with the given scan value.
     */
    public Font getFont(int code) {
	if (tokenFonts == null) {
	    tokenFonts = new Font[JavaToken.MaximumScanValue + 1];
	}
	if (code < tokenFonts.length) {
	    Font f = tokenFonts[code];
	    if (f == null) {
		Style s = tokenStyles[code];
		f = getFont(s);
	    }
	    return f;
	}
	return null;
    }

    /**
     * Fetches the attribute set to use for the given
     * scan code.  The set is stored in a table to
     * facilitate relatively fast access to use in 
     * conjunction with the scanner.
     */
    public Style getStyleForScanValue(int code) {
	if (code < tokenStyles.length) {
	    return tokenStyles[code];
	}
	return null;
    }

    // --- ViewFactory methods -------------------------------------

	public View create(Element elem) {
		return new JavaView(elem, this);
	}

    // --- variables -----------------------------------------------

    /**
     * The styles representing the actual token types.
     */
    Style[] tokenStyles;

    /**
     * Cache of foreground colors to represent the 
     * various tokens.
     */
    transient Color[] tokenColors;

    /**
     * Cache of fonts to represent the various tokens.
     */
    transient Font[] tokenFonts;

    /**
     * View that uses the lexical information to determine the
     * style characteristics of the text that it renders.  This
     * simply colorizes the various tokens and assumes a constant
     * font family and size.
     */
}
