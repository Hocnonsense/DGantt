package dgantt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * A basic implementation of a {@link GanttRenderer}.
 * @author Hwrn
 */
public class DefaultGanttRenderer implements GanttRenderer {

/**
 * The background color for odd numbered rows.
 */
public static final Color ODD_COLOR = new Color(240, 240, 240);

/**
 * The background color for even numbered rows.
 */
public static final Color EVEN_COLOR = Color.WHITE;

/**
 * The foreground color of tasks.
 */
public static final Color FOREGROUND = Color.BLACK;

/**
 * The background color of tasks.
 */
public static final Color BACKGROUND = Color.RED;

/**
 * The selected color of tasks.
 */
public static final Color SELECTED = Color.RED; 

/**
 * Class constructor for a basic row, task and link renderer.
 * @return 
 */
public DefaultGanttRenderer() {
    super();
    
}
  
/**
 * Paint background.
 */
@Override
public void paintBackground(
    final Graphics g, final GanttChart chart) {
  final Graphics2D g2 = (Graphics2D) g;
//get the bounding rectangle of the current clipping area.
  final Rectangle clip = g2.getClipBounds();
  
  g2.setColor(chart.getBackground());
  g2.fill(clip);

}

/**
 * Paint row of two colors.
 */
@Override
public void paintRow(
    final Graphics g, final GanttChart chart, final int row,
    final Rectangle2D bounds, final boolean isTable) {
  final Graphics2D g2 = (Graphics2D) g;
  
  if (row % 2 == 1) {
      g2.setColor(EVEN_COLOR);
  } else {
      g2.setColor(ODD_COLOR);
  }
  
  g2.fill(bounds);

}

/**
 * Paint tasks.
 */
@Override
public final void paintTask(
    final Graphics g, final GanttChart chart, final TaskModel task,
    final Rectangle2D bounds, final boolean selected) {
  final Graphics2D g2 = (Graphics2D) g;

  g2.setColor(BACKGROUND);
  g2.fill(bounds);
  
  g2.setColor(FOREGROUND);
  g2.draw(bounds);
  
  if (selected) {
    g2.setColor(SELECTED);
    g2.draw(bounds);
  }
  
  g2.setColor(FOREGROUND);
  TextUtilities.paintString(g2,
      TaskModel.getName(task), bounds.getBounds(),
      TextUtilities.CENTER, TextUtilities.CENTER);

}

// The following is modified from 
// http://forum.java.sun.com/thread.jsp?forum=57&thread=374342
/**
 * For getArrow.
 * 
 * @param len
 * @param dir
 * @return xCor
 */
private static int xCor(final int len, final double dir) {
  return (int) (len * Math.sin(dir));
}

/**
 * For getArrow.
 * @param len
 * @param dir
 * @return yCor
 */
private static int yCor(final int len, final double dir) {
  return (int) (len * Math.cos(dir));
}

/**
 * Draw a arrow.
 * @param xCenter
 * @param yCenter
 * @param x
 * @param y
 * @return an Arrow graph to paint
 */
//CHECKSTYLE:OFF:MagicNumber
private Polygon getArrow(final int xCenter, final int yCenter,
    final int x, final int y) {
  double aDir = Math.atan2(xCenter-x,yCenter-y);
  
  if ((xCenter - x == 0) && (yCenter - y == 0)) {
    //force the arrow to be horizontal left-to-right when no change
    aDir = -1.5707963267948966; 
  }
  
  Polygon tmpPoly = new Polygon();
  tmpPoly.addPoint(xCenter, yCenter);
  // make the arrow head the same size regardless of the length
  final int i1 = 9;
  final int i2 = 6;
  tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
  tmpPoly.addPoint(
      x + xCor(i1, aDir + 0.5), y + yCor(i1, aDir + 0.5));
  tmpPoly.addPoint(x, y);              // arrow tip
  tmpPoly.addPoint(
      x + xCor(i1, aDir - 0.5), y + yCor(i1, aDir - 0.5));
  tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
  return tmpPoly;
}

@Override
public void paintLink(final Graphics g,
    final GanttChart chart, final TaskLinkModel link) {
  Graphics2D g2 = (Graphics2D) g;
  final Rectangle2D bounds1 = chart.getTaskBounds(link.getLast());
  final Rectangle2D bounds2 = chart.getTaskBounds(link.getNext());
  Shape arrow = null;
  
  switch (link.getType()) {
  case TaskLinkModel.START_TO_FINISH:
    arrow = getArrow(
        (int) bounds1.getMinX(), (int) bounds1.getCenterY(), 
        (int) bounds2.getMaxX(), (int) bounds2.getCenterY());
    break;
    
  case TaskLinkModel.START_TO_START:
    arrow = getArrow(
        (int) bounds1.getMinX(), (int) bounds1.getCenterY(), 
        (int) bounds2.getMinX(), (int) bounds2.getCenterY());
    break;
    
  case TaskLinkModel.FINISH_TO_START:
    arrow = getArrow(
        (int) bounds1.getMaxX(), (int) bounds1.getCenterY(), 
        (int) bounds2.getMinX(), (int) bounds2.getCenterY());
    break;
    
  case TaskLinkModel.FINISH_TO_FINISH:
    arrow = getArrow(
        (int) bounds1.getMaxX(), (int) bounds1.getCenterY(), 
        (int) bounds2.getMaxX(), (int) bounds2.getCenterY());
    break;
    
  default:
    throw new IllegalStateException();
    
  }
  g2.setColor(SELECTED);
  g2.fill(arrow);
  g2.setColor(FOREGROUND);
  g2.draw(arrow);
  
}
  
}
