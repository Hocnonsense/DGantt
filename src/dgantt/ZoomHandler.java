package dgantt;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basic mouse handler for controlling the zoom level of
 *  a Gantt chart.
 * 
 * @author Hwrn
 */
public class ZoomHandler extends MouseAdapter {

/**
 * Start point once press left button.
 */
private Point startpoint;

/**
 * End point once releace left button.
 */
private Point lastpoint;

/**
 * the GanttChart.
 */
private GanttChart chart;

/**
 * Zoom in and out.
 * @param chart
 */
//CHECKSTYLE:OFF:HiddenField
public ZoomHandler(GanttChart chart) {
    this.chart = chart;
}

/**
 * draw the line of the init site
 */
@Override
public void mousePressed(MouseEvent e) {
  if (e.isConsumed()
      || e.getButton() != MouseEvent.BUTTON3)
    return;

  startpoint = e.getPoint();
  Graphics g = chart.getGraphics();
  g.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
  g.drawLine(startpoint.x, 0,
      startpoint.x, chart.getPreferredSize().height);
  g.setPaintMode();
  
  e.consume();

}

/**
 * Caculate the X coordinate and width of the Rectangle.
 * 
 * @param r
 * @param zoom 
 */
public static void zoomRectangle(Rectangle r, double zoom) {
  r.x = (int)(zoom*r.x);
  r.width = (int)(zoom*r.width);

}

@Override
public void mouseReleased(MouseEvent e) {
  if (e.isConsumed()
      || (null == startpoint))
    return;
  
  Graphics g = chart.getGraphics();
  g.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
  if (null != lastpoint)
    g.drawLine(lastpoint.x, 0,
        lastpoint.x, chart.getPreferredSize().height);
  g.drawLine(startpoint.x, 0,
      startpoint.x, chart.getPreferredSize().height);
  g.setPaintMode();

  lastpoint = e.getPoint();
  if (Math.abs(lastpoint.getX() - startpoint.getX()) < 5) {
    //do nothing
    
  } else if (startpoint.getX() < lastpoint.getX()) {
    // move right
    long min = chart.screenToCanonical(startpoint.getX());
    long max = chart.screenToCanonical(lastpoint.getX());
    double relZoom = (
        (double) (chart.getRange()) / (double) (max-min))
        * (1.0 / chart.getZoom());
    
    // this chunk of code computes the new visible rectangle after
    //  zoom
    Rectangle visibleRect = chart.getVisibleRect();
    Rectangle zoomRect = new Rectangle(
        startpoint.x, 0,
        lastpoint.x, chart.getPreferredSize().height);
    Rectangle intersection = visibleRect.intersection(zoomRect);
    zoomRectangle(intersection, relZoom);
    
    chart.setZoom(chart.getZoom()*relZoom);
    chart.scrollRectToVisible(intersection);
    
  } else {
    chart.setZoom(1.0);
    chart.scrollRectToVisible(
        new Rectangle(new Point(0, 0), chart.getPreferredSize()));
    
  }
  startpoint = null;
  lastpoint = null;

  e.consume();

}

/**
 * draw another line following mouse
 */
@Override
public void mouseDragged(MouseEvent e) {
  if (e.isConsumed()
      || (startpoint == null))
  return;
  
  Graphics g = chart.getGraphics();
  g.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
  if (lastpoint != null)
    g.drawLine(lastpoint.x, 0,
        lastpoint.x, chart.getPreferredSize().height);

  lastpoint = e.getPoint();
  g.drawLine(lastpoint.x, 0,
    lastpoint.x, chart.getPreferredSize().height);
  g.setPaintMode();

  e.consume();

  
}

}
