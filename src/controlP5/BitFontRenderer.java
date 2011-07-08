package controlP5;

/**
 * controlP5 is a processing gui library.
 *
 *  2007-2011 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author 		Andreas Schlegel (http://www.sojamo.de)
 * @modified	##date##
 * @version		##version##
 *
 */

/**
 * adopted from fasttext by Glen Murphy @ http://glenmurphy.com/
 */

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import processing.core.PImage;

/**
 * The bitfontRenderer is used to draw any text labels used by controlP5 by
 * default. The bitfontRenderer is based on a per pixel technique and is not
 * using processing's PFont renderer. To use PFonts within controlP5, take a
 * look at ControlFont
 * 
 * @see controlP5.ControlFont
 * 
 * 
 */
public class BitFontRenderer {
	/**
	 * ftext - fast text for processing. to create a font graphic use the
	 * following string (first character being a space)
	 * !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`
	 * abcdefghijklmnopqrstuvwxyz{|}~
	 * 
	 * free fonts http://www.dafont.com/advocut.font
	 * http://www.dafont.com/grixel-kyrou-9.font
	 * http://www.dafont.com/david-sans.font
	 * http://www.dafont.com/sven-stuber.d516
	 * http://www.dafont.com/supernatural.font
	 * http://www.dafont.com/supertext.font http://www.dafont.com/regupix.font
	 * http://www.dafont.com/optiate.font http://www.dafont.com/superhelio.font
	 * http://www.dafont.com/superbly.font
	 * http://www.fontsquirrel.com/fonts/Audimat-Mono
	 * http://www.fontsquirrel.com/fonts/Envy-Code-R
	 */

	protected static int numFonts = 4;

	public static final int standard58 = ControlP5.standard58;

	public static final int standard56 = ControlP5.standard56;

	public static final int synt24 = ControlP5.synt24;

	public static final int grixel = ControlP5.grixel;

	protected float height;

	protected static Map<Integer, BitFont> fonts;

	protected BitFontRenderer(final Component theComponent) {
		loadFonts(theComponent);
	}

	public BitFontRenderer() {
		loadFonts(null);
	}

	private void loadFonts(Component theComponent) {
		if (fonts == null) {
			fonts = new HashMap<Integer, BitFont>();
			fonts.put(standard58, new BitFont(standard58).setSource("standard58.gif"));
			fonts.put(standard56, new BitFont(standard56).setSource("standard56.gif"));
			fonts.put(synt24, new BitFont(synt24).setSource("synt24.gif"));
			fonts.put(grixel, new BitFont(grixel).setSource("GrixelKyrou9.gif"));
		}
	}

	/**
	 * TODO implement addBitFont
	 * 
	 * @param theImage
	 * @return
	 */
	public int addBitFont(PImage theImage) {
		// TODO
		return -1;
	}

	protected BitFont getFont(int theIndex) {
		return fonts.get(theIndex);
	}

	protected static int getPosition(String theText, Label.BitFontLabel theLabel, int theX) {
		theText = (theText == null) ? " " : theText;

		BitFont f = fonts.get(theLabel.getFontIndex());
		theText = (theLabel.isToUpperCase()) ? theText.toUpperCase() : theText;
		int l = theText.length();
		int x = 0;
		for (int i = 0; i < l; i++) {
			final int myIndex = ((int) theText.charAt(i) - 32);
			if (myIndex >= 0 && myIndex <= 95) {
				x += f.charWidth[myIndex] + theLabel.getLetterSpacing();
				if(x>=theX) {
					return i;
				}
			}
		}
		return l;
	}

	/**
	 * get the width of a text line based on the bit font used.
	 * 
	 * @param theText
	 * @param theFontIndex
	 * @return
	 */
	public static int getWidth(Label.BitFontLabel theLabel) {
		return getWidth(theLabel.getText(), theLabel, theLabel.getText().length());
	}

	public static int getWidth(String theText, final Label.BitFontLabel theLabel) {
		return getWidth(theText, theLabel, theText.length());
	}

	protected static int getWidth(String theText, final Label.BitFontLabel theLabel, int theLength) {
		return getDimension(theText, theLabel, theLength)[0];
	}

	protected static int[] getDimension(String theText, final Label.BitFontLabel theLabel) {
		return getDimension(theText, theLabel, theText.length());
	}

	protected static int[] getDimension(String theText, final Label.BitFontLabel theLabel, int theLength) {
		int[] dim = { 0, theLabel.getLineHeight() };
		int tx = 0;
		theText = (theText == null) ? " " : theText;

		BitFont f = fonts.get(theLabel.getFontIndex());

		theText = (theLabel.isToUpperCase()) ? theText.toUpperCase() : theText;
		for (int i = 0; i < theLength; i++) {
			final int myIndex = ((int) theText.charAt(i) - 32);
			if (myIndex >= 0 && myIndex <= 95) {
				dim[0] += f.charWidth[myIndex] + theLabel.getLetterSpacing();
			} else {
				int c = theText.charAt(i);
				if (c != 9 && c != 10 && c != 13) {
					// 9 = tab, 10 = new line, 13 = carriage return
					ControlP5.logger().warning("You are using a character that is not supported by controlP5's BitFont-Renderer, you could use ControlFont instead (see the ControlP5controlFont example). ("
							+ ((int) theText.charAt(i)) + "," + theText.charAt(i) + ")");
				} else {
					if (dim[0] > tx) {
						tx = dim[0];
						dim[0] = 0;
					}
					if (c == 10 || c == 13) {
						dim[1] += theLabel.getLineHeight();
					}
				}
			}
		}
		dim[0] = (dim[0] > tx) ? dim[0] : tx;
		return dim;
	}

	public static int getHeight(int theFontIndex) {
		return fonts.get(theFontIndex).texture.height;
	}

	public static int getHeight(Label.BitFontLabel theLabel) {
		return fonts.get(theLabel.getFontIndex()).texture.height;
	}

	private static void putchar(
			final int theC,
			final int theX,
			final int theY,
			final int theColor,
			boolean theHighlight,
			final PImage theImage,
			final PImage theMask,
			final BitFont theBitFont) {
		final int myWH = theImage.width * theImage.height;
		final int len = theBitFont.charWidth[theC] * theBitFont.charHeight;
		final int w = theY * theImage.width;
		for (int i = 0; i < len; i++) {
			final int xpos = theX + i % theBitFont.charWidth[theC];
			final int pos = xpos + w + (i / theBitFont.charWidth[theC]) * theImage.width;
			if (theBitFont.chars[theC][i] == 0xff000000 && xpos < theImage.width && xpos >= 0 && pos >= 0 && pos < myWH) {
				theImage.pixels[pos] = theColor;
				theMask.pixels[pos] = 0xffffffff;
			}
		}
	}

	private static int writeCharacters(final Label.BitFontLabel theLabel) {

		int indent = 0;

		final int myOriginalY = theLabel.getOffsetY();

		int myY = theLabel.getOffsetY();

		final String myText = theLabel.isToUpperCase() ? theLabel.getText().toUpperCase() : theLabel.getText();

		int myWrap = (theLabel.isMultiline()) ? theLabel.getImage().width : -1;

		int l = myText.length();

		final int[] letters_indent = new int[l];
		final int[] letters_letter = new int[l];
		final boolean[] letters_isHighlight = new boolean[l];
		final int[] letters_lineheight = new int[l];

		int err = 0;

		BitFont f = fonts.get(theLabel.getFontIndex());

		for (int i = 0; i < l; i++) {

			int c = (int) myText.charAt(i);

			if (c != 10) {
				if ((myWrap > 0 && indent > myWrap)) {
					indent = theLabel.getOffsetX(); // 0;
					myY += theLabel.getLineHeight();
					final int j = i;
					err++;
					while (i > 0 && err < myText.length()) {
						i--;
						// in case a word longer than the actual width.
						if (i == 1) {
							i = j;
							break;
						}
						// go back until you find a space or a dash.
						if (myText.charAt(i) == ' ' || myText.charAt(i) == '-') {
							i++;
							c = (int) myText.charAt(i);
							break;
						}
					}
				}

				if (c >= 127 || c <= 32) {
					c = 32;
				}

				letters_indent[i] = indent;
				letters_letter[i] = c - 32;
				letters_isHighlight[i] = (i == theLabel.getCursorPosition() - 1);
				letters_lineheight[i] = myY;

				indent += f.charWidth[c - 32] + theLabel.getLetterSpacing();
			} else {
				myY += theLabel.getLineHeight();
				indent = 0;
				letters_indent[i] = 0;
				letters_letter[i] = -1;
				letters_isHighlight[i] = false;
				letters_lineheight[i] = 0;
			}
		}
		for (int i = 0; i < l; i++) {
			if (letters_letter[i] != -1) {
				putchar(letters_letter[i], theLabel.getOffsetX() + letters_indent[i], letters_lineheight[i], theLabel.getColor(), letters_isHighlight[i], theLabel.getImage(), theLabel.getImageMask(), f);
			}
		}
		return myY - myOriginalY;
	}

	/*
	 * (non-Javadoc)
	 */
	public static int write(final Label.BitFontLabel theLabel) {
		final int myWH = theLabel.getImage().width * theLabel.getImage().height;
		for (int i = 0; i < myWH; i++) {
			theLabel.getImage().pixels[i] = 0x00ffffff;
			theLabel.getImageMask().pixels[i] = 0xff000000;
		}
		final int myHeight = writeCharacters(theLabel);
		theLabel.getImage().mask(theLabel.getImageMask());
		return myHeight;
	}

	class BitFont {

		protected int characters;

		protected int[] charWidth = new int[255];

		protected int charHeight;

		protected int[][] chars;

		protected int lineHeight;

		protected int wh;

		protected PImage texture;

		protected int id;

		private String _mySource;

		BitFont(int theId) {
			id = theId;
		}

		BitFont setSource(String theSource) {
			_mySource = theSource;
			texture = ControlP5.papplet.loadImage(getClass().getResource(_mySource).toString());
			charHeight = texture.height;
			lineHeight = charHeight;
			int currWidth = 0;
			int maxWidth = 0;

			for (int i = 0; i < texture.width; i++) {
				currWidth++;
				if (texture.pixels[i] == 0xffff0000) {
					charWidth[characters] = currWidth;
					characters++;
					if (currWidth > maxWidth) {
						maxWidth = currWidth;
					}
					currWidth = 0;
				}
			}

			// create the character sprites.
			chars = new int[characters][maxWidth * charHeight];
			int indent = 0;
			for (int i = 0; i < characters; i++) {
				for (int u = 0; u < charWidth[i] * charHeight; u++) {
					chars[i][u] = texture.pixels[indent + (u / charWidth[i]) * texture.width + (u % charWidth[i])];
				}
				indent += charWidth[i];
			}
			return this;
		}

	}
}
