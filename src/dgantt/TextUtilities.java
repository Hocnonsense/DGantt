package dgantt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Provides lightweight plain text rendering methods. Specifically,
 *  provides methods to render single-line strings and multi-line
 *  text inside a rectangle. Ellipsis replace text not contained
 *  in the given region.
 */
public final class TextUtilities {

  /**
   * Center-aligned text.
   */
  public static final int CENTER = SwingUtilities.CENTER;

  /**
   * Left-aligned text.
   */
  public static final int LEFT = SwingUtilities.LEFT;

  /**
   * Right-aligned text.
   */
  public static final int RIGHT = SwingUtilities.RIGHT;

  /**
   * Top-aligned text.
   */
  public static final int TOP = SwingUtilities.TOP;

  /**
   * Bottom-aligned text.
   */
  public static final int BOTTOM = SwingUtilities.BOTTOM;

  /**
   * Renders a single line of text. 
   *  Differs from Graphics.drawString(...) by anchoring rendered
   *  text at the upper-left corner rather than the baseline.
   *
   * @param g graphics target
   * @param text string to render
   * @param p upper-left corner of rendered text
   */
  //CHECKSTYLE:OFF:FinalParameters
  public static void paintString(
      final Graphics g, String text, final Point2D p) {
    g.drawString(text, (int) p.getX(), (int) p.getY() + 
        g.getFontMetrics().getAscent());
    
  }
  
  /**
   * Renders a single line of text. The text is rendered inside
   *  a bounding box allowing alignment and clipping.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @param halign horizontal alignment, either LEFT, CENTER, RIGHT
   * @param valign vertical alignment, either TOP, CENTER, BOTTOM
   */
  //CHECKSTYLE:OFF:FinalParameters
  public static void paintString(
      final Graphics g, String text, final Rectangle bounds,
      final int halign, final int valign) {
    paintString(g, text, bounds, halign, valign, false);
    
  }

  /**
   * Renders a single line of text. The text is rendered inside
   *  a bounding  box allowing alignment and clipping.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @param halign horizontal alignment, either LEFT, CENTER, RIGHT
   * @param valign vertical alignment, either TOP, CENTER, BOTTOM
   * @param force force rendering of string even if it doesn't
   *    fit in bounds
   */
  //CHECKSTYLE:OFF:FinalParameters
  public static void paintString(
      final Graphics g, String text, final Rectangle bounds, 
      final int halign, final int valign, final boolean force) {
    final FontMetrics fm = g.getFontMetrics();

    if (!force && (fm.getHeight() > bounds.getHeight())) {
      return;
      
    }

    if (fm.stringWidth(text) > bounds.getWidth()) {
      text = clipString(null, fm, text, (int) bounds.getWidth());
      
    }

    int hoffset = 0;
    switch (halign) {
    case LEFT:
      //do nothing
      break;
    case CENTER:
      hoffset = (bounds.width - fm.stringWidth(text)) / 2;
      break;
    case RIGHT:
      hoffset = bounds.width - fm.stringWidth(text);
      break;
    default:
      throw new IllegalStateException();
      
    }

    int voffset = 0;
    switch (valign) {
    case TOP:
      //do nothing
      break;
    case CENTER:
      voffset = (bounds.height - fm.getHeight()) / 2;
      break;
    case BOTTOM:
      voffset = bounds.height - fm.getHeight();
      break;
    default:
      throw new IllegalStateException();
      
    }

    paintString(g, text, new Point(
        bounds.x + hoffset, bounds.y + voffset));
    
  }

  /**
   * Renders multi-line text.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @param halign horizontal alignment, either LEFT, CENTER, RIGHT
   * @param valign vertical alignment, either TOP, CENTER, BOTTOM
   */
  public static void paintText(
      final Graphics g, final String text, final Rectangle bounds, 
      final int halign, final int valign) {     
    final Point pen = bounds.getLocation();
    final Graphics2D g2 = (Graphics2D) g;
    final FontMetrics fm = g2.getFontMetrics();
    final List<String> lines = splitText(g, text, bounds);

    switch (valign) {
    case TOP:
      //do nothing
      break;
    case CENTER:
      pen.y += (bounds.height - fm.getHeight() * lines.size()) / 2;
      break;
    case BOTTOM:
      pen.y += bounds.height - fm.getHeight() * lines.size();
      break;
    default:
      throw new IllegalStateException();
      
    }

    for (int i = 0; i < lines.size(); i++) {
      paintString(g, lines.get(i), new Rectangle(pen.x, pen.y, 
          bounds.width, fm.getHeight()), halign, TOP);
      pen.y += fm.getHeight();
      
    }
    
  }

  /**
   * Splits text into individual lines fitting in a given
   *  rectangular bounds.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @return list of individual lines
   */
  protected static List<String> splitText(
      final Graphics g, final String text, final Rectangle bounds) {
    final Point pen = bounds.getLocation();
    final Graphics2D g2 = (Graphics2D) g;
    final FontMetrics fm = g2.getFontMetrics();
    final ArrayList<String> result = new ArrayList<String>();
    int index = 0;
    
    while (index < text.length()) {
      final int end =
          getLineBreakIndex(g, text, index, bounds.getWidth());
      
      if (end == index) {
        result.add("...");
        break;
        
      }
      
      String str = text.substring(index, end).trim();
      index = end;

      if ((pen.getY() + fm.getHeight() + fm.getHeight()) >
          (bounds.getY() + bounds.getHeight())) {
        if (end < text.length()) {
          str = clipString(null, fm, str, (int) bounds.getWidth());
          
        }
        
        result.add(str);
        break;
        
      }

      result.add(str);
      pen.y += fm.getHeight();
      
    }
    
    return result;
    
  }

  /**
   * Returns {@code true} if the text can be rendered inside
   *  a given bounds without clipping; {@code false} otherwise.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @return boolean indicating text is renderable in bounds
   */
  public static boolean isTextInBounds(
      final Graphics g, final String text, final Rectangle bounds) {
    final Point pen = bounds.getLocation();
    final Graphics2D g2 = (Graphics2D)g;
    final FontMetrics fm = g2.getFontMetrics();
    int index = 0;
    
    while (index < text.length()) {
      final int end =
          getLineBreakIndex(g, text, index, bounds.getWidth());
      
      if (end == index) {
        return false;
        
      }
      
      index = end;

      if ((pen.getY() + fm.getHeight() + fm.getHeight()) >
          (bounds.getY() + bounds.getHeight())) {
        if (index < text.length()) {
          return false;
          
        }
        
      }
      
      pen.y += fm.getHeight();
      
    }
    
    return true;
  }

  /**
   * Binary search for optimal font size.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @param size current font size
   * @param minSize minimum font size
   * @param maxSize maximum font size
   * @return optimal font size
   */
  private static int findFontSizeBinarySearch(
      final Graphics g, final String text, final Rectangle bounds,
      final int size, final int minSize, final int maxSize) {
    if (maxSize - minSize <= 1) {
      return size;
      
    }

    final Font font =
        new Font(g.getFont().getName(), g.getFont().getStyle(), size);
    g.setFont(font);
    
    if (isTextInBounds(g, text, bounds)) {
      return findFontSizeBinarySearch(
          g, text, bounds, (maxSize + size) / 2, size, maxSize);
      
    } else {
      return findFontSizeBinarySearch(
          g, text, bounds, (minSize + size) / 2, minSize, size);
      
    }
    
  }

  /**
   * Determines the optimal font size for fitting the given text
   *  inside the given bounds.
   *  A binary search is used between minSize and maxSize.
   * 
   * @param g graphics target
   * @param text string to render
   * @param bounds rectangle in which to render text
   * @param minSize minimum font size
   * @param maxSize maximum font size
   * @return optimal font size
   */
  public static int findFontSize(
      final Graphics g, final String text, final Rectangle bounds, 
      final int minSize, final int maxSize) {
    final Font font = g.getFont();
    final int size = findFontSizeBinarySearch(
        g, text, bounds, (minSize + maxSize) / 2, minSize, maxSize);
    g.setFont(font);
    return size;
    
  }

  /**
   * Computes the index of the next line break.  Similar to
   *  LineBreakMeasurer but modified due to a flaw in
   *  Sun's implementation.
   * 
   * @param g graphics target
   * @param text string to render
   * @param index last line break index
   * @param maxWidth maximum width of rendered line
   * @return index of next line break
   */
  public static int getLineBreakIndex(
      final Graphics g, final String text, final int index, 
      final double maxWidth) {
    final FontMetrics fm = g.getFontMetrics();
    int i = index;
    double width = 0.0;

    //find last index of satisfiable width
    while (i < text.length()) {
      int codePoint = text.codePointAt(i);
      width += fm.charWidth(codePoint);
      
      if (width >= maxWidth) { //&& Character.isWhitespace(codePoint))
        break;
      }
      
      i++;
    }

    if (i == text.length()) {
      return i;
    }

    //find last whitespace index
    while (i > index) {
      int codePoint = text.codePointAt(i);
      
      if (!Character.isLetterOrDigit(codePoint)) {
        break;
      }
      
      i--;
    }

    return i;
  }

  /*
   * The following code is modified from
   *  com.sun.java.swing.SwingUtilities2.
   */

  // all access to charsBuffer is to be synchronized on
  //  charsBufferLock
  private static final int CHAR_BUFFER_SIZE = 100;
  private static final Object charsBufferLock = new Object();
  private static char[] charsBuffer = new char[CHAR_BUFFER_SIZE];

  private static final boolean isComplexLayout(char ch) {
    return (ch >= '\u0900' && ch <= '\u0D7F') || // Indic
        (ch >= '\u0E00' && ch <= '\u0E7F') || // Thai
        (ch >= '\u1780' && ch <= '\u17ff') || // Khmer
        (ch >= '\uD800' && ch <= '\uDFFF');   // surrogate value range
    
  }

  /**
   * modified from com.sun.java.swing.SwingUtilities2.
   * @param ch
   * @return
   */
  private static final boolean isSimpleLayout(char ch) {
    return ch < 0x590 || (0x2E00 <= ch && ch < 0xD800);
  }

  /**
   * modified from com.sun.java.swing.SwingUtilities2.
   * @param text
   * @param start
   * @param limit
   * @return
   */
  public static final boolean isComplexLayout(
      char[] text, int start, int limit) {
    boolean simpleLayout = true;
    char ch;
    for (int i = start; i < limit; ++i) {
      ch = text[i];
      if (isComplexLayout(ch)) {
        return true;
        
      }
      if (simpleLayout) {
        simpleLayout = isSimpleLayout(ch);
      }
    }
    if (simpleLayout) {
      return false;
    }
    return Bidi.requiresBidi(text, start, limit);
  }

  /**
   * modified from com.sun.java.swing.SwingUtilities2.
   * @param c
   * @param fm
   * @param string
   * @param availTextWidth
   * @return
   */
  public static String clipString(JComponent c, FontMetrics fm,
      String string, int availTextWidth) {
    //c may be null here.
    String clipString = "...";
    int stringLength = string.length();
    availTextWidth -= fm.stringWidth(clipString);
    boolean needsTextLayout = false;

    synchronized (charsBufferLock) {
      if (charsBuffer == null || charsBuffer.length < stringLength) {
        charsBuffer  = string.toCharArray();
      } else {
        string.getChars(0, stringLength, charsBuffer, 0);
      }
      needsTextLayout = 
        isComplexLayout(charsBuffer, 0, stringLength);
      if (!needsTextLayout) {
        int width = 0;
        for (int nChars = 0; nChars < stringLength; nChars++) {
          width += fm.charWidth(charsBuffer[nChars]);
          if (width > availTextWidth) {
            string = string.substring(0, nChars);
            break;
          }
        }
      }
    }
    
    if (needsTextLayout) {
      throw new RuntimeException("does not support complex text layout");
    }
    
    if (string.isEmpty()) {
      return "";
    } else {
      return string + clipString;
    }
  }

    /**
     * Hide Utility Class Constructor.
     */
    private TextUtilities() {
      throw new IllegalStateException("Utility class");

    }
  
}
