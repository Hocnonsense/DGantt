package dgantt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Basic {@link MouseAdapter} for selecting multiple tasks in a
 *  Gantt chart.
 *  
 * @author Hwrn
 */
public class SelectionHandler extends MouseAdapter {

/**
 * The {@link GanttChart} connected to this listener.
 */
private final GanttChart chart;

/**
 * The starting point of the selection; or {@code null} if no selection
 * is currently in progress.
 */
private Point startpoint;

/**
 * The bounds of the last selection box.  This is used by the XOR painting
 * to efficiently remove the previously drawn selection box.
 */
private Rectangle2D lastbox;

/**
 * Class constructor for selecting multiple tasks in a Gantt chart.
 * 
 * @param chart the {@code GanttChart} connected to this listener
 */
//CHECKSTYLE:OFF:HiddenField
public SelectionHandler(final GanttChart chart) {
    super();
    this.chart = chart;
}

/**
 * If click at blank.
 * get start point of the selected area
 */
@Override
public void mousePressed(final MouseEvent e) {
    if (e.isConsumed() 
            || e.isControlDown()) {
        return;
    }
    
    if ((e.getButton() == MouseEvent.BUTTON1) 
            && (e.getClickCount() == 1) 
            && (chart.getTaskAt(e.getPoint()) == null)) {
        startpoint = e.getPoint();
        e.consume();
    }
}

@Override
public final void mouseReleased(final MouseEvent e) {
    if (e.isConsumed() 
            || (startpoint == null)) {
        return;
    }
    
    final Graphics2D g2 = (Graphics2D) chart.getGraphics();
    g2.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
    g2.setStroke(MagicNumber.BOX_STROKE);

    chart.getSelectedTasks().clear();
    
    if (lastbox != null) {
      g2.draw(lastbox);
      for (int i = chart.getTaskSetModel().getRowCount() - 1;
          i >= 0; --i) {
        for (int j = chart.getTaskSetModel().getTaskCount(i) - 1;
            j >= 0; --j) {
          final TaskModel task =
              chart.getTaskSetModel().getTaskAt(i, j);
          final Rectangle2D bounds = chart.getTaskBounds(task);
          if (bounds.intersects(lastbox)) {
              chart.getSelectedTasks().add(task);
              
          }
          
        }
        
      }
      
    }
    startpoint = null;
    lastbox = null;     
    chart.repaint();
    chart.fireSelectionEvent();
    
    e.consume();
    
}

@Override
public final void mouseDragged(final MouseEvent e) {
    if (e.isConsumed() || (startpoint == null)) {
        return;
        
    }
    
    final Point endpoint = e.getPoint();
    Graphics2D g2 = (Graphics2D) chart.getGraphics();
    g2.setXORMode(Color.WHITE);
    g2.setStroke(MagicNumber.BOX_STROKE);
    
    if (lastbox != null) {
        g2.draw(lastbox);
        
    }
    lastbox = new Rectangle2D.Double(
            Math.min(startpoint.getX(), endpoint.getX()),
            Math.min(startpoint.getY(), endpoint.getY()),
            Math.abs(endpoint.getX() - startpoint.getX()), 
            Math.abs(endpoint.getY() - startpoint.getY()));
    g2.draw(lastbox);
    
    e.consume();
    
}

}
