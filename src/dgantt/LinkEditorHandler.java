package dgantt;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * A {@link MouseAdapter} for add links using the right-mouse buttton.
 * @author DELL
 *
 */
public class LinkEditorHandler extends MouseAdapter {

/**
 * The source task.
 */
private TaskModel selectedtask;

/**
 * The current position of the mouse cursor.
 */
private Point selectedpoint;

/**
 * The last position of the mouse cursor, used for clearing the
 *  previously rendered ghost line.
 */
private Point lastpoint;

/**
 * The target task.
 */
private TaskModel targettask;

/**
 * The bounds of the target task, used for rendering an outline.
 */
private Rectangle2D targetbounds;

/**
 * The {@code GanttChart} connected to this listener.
 */
private final GanttChart chart;

/**
 * Class constructor for adding links.
 * 
 * @param chart
 */
//CHECKSTYLE:OFF:HiddenField
public LinkEditorHandler(GanttChart chart) {
  super();
  this.chart = chart;

}

@Override
public void mousePressed(MouseEvent e) {
  if (e.isConsumed()
      || e.getButton() != MouseEvent.BUTTON3) {
    return;
  
  }
  selectedpoint = e.getPoint();
  selectedtask = chart.getTaskAt(selectedpoint);

  if (selectedtask == null) {
    selectedpoint = null;
    return;
    
  }
  lastpoint = selectedpoint;

  Graphics2D g2 = (Graphics2D)chart.getGraphics();
  g2.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
  g2.draw(new Line2D.Double(selectedpoint, lastpoint));
  g2.fill(new Rectangle(selectedpoint.x-2, selectedpoint.y-2, 5, 5));
  g2.setPaintMode();

  chart.setCursor(Cursor.getPredefinedCursor(
      Cursor.CROSSHAIR_CURSOR));

  e.consume();

}

@Override
public void mouseReleased(MouseEvent e) {
  if (e.isConsumed() || (selectedpoint == null)) {
    return;
    
  }
  Graphics2D g2 = (Graphics2D)chart.getGraphics();
  g2.setXORMode(MagicNumber.BACKGROUND_COLOR_2);

  if (targetbounds != null) {
      g2.draw(targetbounds);
  }

  g2.draw(new Line2D.Double(selectedpoint, lastpoint));
  g2.fill(new Rectangle(selectedpoint.x-2, selectedpoint.y-2, 5, 5));
  g2.setPaintMode();

  chart.setCursor(Cursor.getDefaultCursor());

  lastpoint = e.getPoint();
  TaskModel hoverTask = chart.getTaskAt(lastpoint);

  if (hoverTask != null) {
      chart.getTaskSetModel().addTaskLink(new BasicTaskLinkModel(
          selectedtask, hoverTask, TaskLinkModel.FINISH_TO_START));
      chart.repaint(chart.getVisibleRect());
  }

  selectedpoint = null;
  selectedtask = null;
  lastpoint = null;
  targettask = null;
  targetbounds = null;

  e.consume();

}

@Override
public void mouseDragged(MouseEvent e) {
  if (e.isConsumed() || (selectedtask == null)) {
    return;
    
  }
  Graphics2D g2 = (Graphics2D)chart.getGraphics();
  g2.setXORMode(MagicNumber.BACKGROUND_COLOR_2);
  g2.draw(new Line2D.Double(selectedpoint, lastpoint));

  lastpoint = e.getPoint();
  TaskModel hoverTask = chart.getTaskAt(lastpoint);

  if ((hoverTask == null) || (hoverTask == selectedtask)) {
    if (targetbounds != null) {
      g2.draw(targetbounds);
      targetbounds = null;
      
    }
    
  } else if (hoverTask != targettask) {
    if (targetbounds != null) {
      g2.draw(targetbounds);
       
    }           
    targettask = hoverTask;
    targetbounds = chart.getTaskBounds(
        targettask, chart.getRow(lastpoint.getY()), 0, 0);
    targetbounds.setRect(
        targetbounds.getX()-1.0, targetbounds.getY()-1.0,
        targetbounds.getWidth()+2.0, targetbounds.getHeight()+2.0);
    g2.draw(targetbounds);
    
  }
  g2.draw(new Line2D.Double(selectedpoint, lastpoint));
  g2.setPaintMode();

  e.consume();
    
}

@Override
public void mouseEntered(MouseEvent e) {
  if (e.isConsumed() || (selectedtask == null)) {
    return;
    
  }
  e.consume();

}

@Override
public void mouseExited(MouseEvent e) {
  if (e.isConsumed() || (selectedtask == null)) {
    return;
    
  }
  e.consume();
  
}

@Override
public void mouseClicked(MouseEvent e) {
  if (e.isConsumed() || (selectedtask == null)) {
    return;
    
  }
  e.consume();
    
}

@Override
public void mouseMoved(MouseEvent e) {
  if (e.isConsumed() || (selectedtask == null)) {
    return;
    
  }
  e.consume();
    
}

}
