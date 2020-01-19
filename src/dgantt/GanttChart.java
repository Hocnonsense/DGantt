package dgantt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A Gantt chart swing component.  A Gantt chart is constructed by
 *  providing a {@link TaskSetModel} and a {@link TaskModel}.
 *  The {@code GanttModel} stores all tasks displayed in the
 *  Gantt chart in addition to controlling several additional
 *  settings. The {@link TaskModel} extracts the necessary
 *  information from user-defined objects, such as the start
 *  and end values of the task, to render the Gantt chart tasks.
 *  An optional {@code LinkModel} can provide visual links between
 *  tasks to represent arbitrary requirements or constraints.
 *  <p>
 *  This component provides the basic Gantt chart functionality,
 *  but is intended to be extended with custom renderers and
 *  listeners to provide more complex behavior.
 * @author Hwrn
 */
public class GanttChart
    extends JComponent implements GanttModelListener {

/**
 * =_=.
 */
private static final long serialVersionUID = 1L;

/**
 * model.
 */
private final TaskSetModel taskset;

/**
 * The renderer used for drawing rows.
 */
private GanttRenderer renderer;

/**
 * The set of tasks currently selected in this Gantt chart.
 */
private Set<TaskModel> selectedtasks;

/**
 * The listeners registered with this Gantt chart to receive
 *  notifications when the set of selected tasks is changed.
 */
private List<GanttSelectionListener> selectionlisteners;

/**
 * The listeners registered with this Gantt chart to receive
 *  notifications when any aspect of the Gantt chart is changed.
 */
private List<ChangeListener> changelisteners;

/**
 * the user-specified zoom.
 * to record size of task body and time axis.
 */
private double zoom;

/**
 * The minimum value displayed in this Gantt chart, in canonical 
 * coordinates.
 */
private long rangeminimum;

/**
 * The maximum value displayed in this Gantt chart, in canonical 
 * coordinates.
 */
private long rangemaximum;

/**
 * draw a gantt chart.
 * body of gantt
 * 
 * @param taskset
 * @param renderer
 */
//CHECKSTYLE:OFF:hiddenField
public GanttChart(final TaskSetModel taskset,
    final GanttRenderer renderer) {
  this.taskset = taskset;
  this.renderer = renderer;

  selectedtasks = new HashSet<TaskModel>();
  selectionlisteners = new Vector<GanttSelectionListener>();
  changelisteners = new Vector<ChangeListener>();

  zoom = 1.0;

  computeRange();
  setToolTipText("");

  taskset.addGanttModelListener(this);
}

/**
 * Computes the minimum and maximum values contained in this
 *  Gantt chart.
 */
private void computeRange() {
  rangeminimum = Long.MAX_VALUE;
  rangemaximum = Long.MIN_VALUE;
  
  for (int i = taskset.getRowCount() - 1; i >= 0; --i) {
    for (int j = taskset.getTaskCount(i) - 1; j >= 0; --j) {
      TaskModel task = taskset.getTaskAt(i, j);
      rangeminimum = Math.min(
          rangeminimum, TaskModel.getStart(task));
      rangemaximum = Math.max(
          rangemaximum, TaskModel.getEnd(task));
      
    }
    
  }
  if ((rangeminimum == Long.MAX_VALUE) 
      || (rangemaximum == Long.MIN_VALUE)) {
    //model empty
    rangeminimum = 0;
    rangemaximum = 0;
    
  }
  
}

public void addGanttSelectionListener(
    GanttSelectionListener linstener) {
  selectionlisteners.add(linstener);
}

/**
 * Forces this Gantt chart to revalidate and repaint, but also
 *  forces any parent {@link JScrollPane} or {@link GanttPanel}
 *  to revalidate and repaint.
 */
protected void forceRevalidateAndRepaint() {
  revalidate();
  repaint();

  if (getParent() instanceof JViewport) {
    JViewport viewport = (JViewport) getParent();
    viewport.revalidate();
    viewport.repaint();

    if (viewport.getParent() instanceof JScrollPane) {
      JScrollPane scrollPane = (JScrollPane) viewport.getParent();
      scrollPane.revalidate();
      scrollPane.repaint();
      
    }
    
  }
  
}

/**
 * Notifies all registered {@link ChangeListener} that
 *  the Gantt chart contents have changed.
 */
public void fireChangeEvent() {
  ChangeEvent event = new ChangeEvent(this);
  
  for (ChangeListener listener : changelisteners) {
    listener.stateChanged(event);
    
  }
  
}

/**
 * Notifies all registered {@link GanttSelectionListener} that the set of
 * selected tasks has changed.
 */
public void fireSelectionEvent() {
  GanttSelectionEvent event = new GanttSelectionEvent(this);
  
  for (GanttSelectionListener listener : selectionlisteners) {
    listener.valueChanged(event);
    
  }
  
}

/**
 * Return the row of the title.
 *  Else -1
 * 
 * @param title the title of task
 * @return the row of task
 */
public int getTitleAt(String title) {
  for (int i = taskset.getRowCount() - 1; i >= 0; --i) {
    if (title == taskset.getTitleAt(i)) {
      return i;
    
    }
  
  }
  return -1;
  
}

/**
 * Return the column index of the task.
 *  Else -1
 *  
 * @param titlerow
 * @param task
 * @return the column index of the specified task
 */
public int getTaskAt(int titlerow, TaskModel task) {
  for (int j = taskset.getTaskCount(titlerow)-1; j>=0; --j) {
    if (task.equals(taskset.getTaskAt(titlerow, j))) {
      return j;
      
    }
    
  }
  return -1;
  
}

/**
 * how to change.
 * check to see if selected tasks still exist
 */
@Override
public void ganttModelChanged(final GanttModelEvent event) {
  Iterator<TaskModel> iterator = selectedtasks.iterator();
  boolean selectionChanged = false;
  
  while (iterator.hasNext()) {
    TaskModel task = iterator.next();
    boolean found = false;
    
    int i = getTitleAt(TaskModel.getTitle(task));
    if (-1 != i) {
      int j = getTaskAt(i, task);
      if (-1 != j) {
        found = true;
        
      }
      
    }
    if (!found) {
      iterator.remove();
      selectionChanged = true;
      
    }
    
  }
  
  computeRange();
  forceRevalidateAndRepaint();
  fireChangeEvent();
  
  if (selectionChanged) {
    fireSelectionEvent();
    
  }
  
}

/**
 * Returns the scaling factor used to convert between canonical and
 *  screen coordinates.
 * 
 * @return the scaling factor used to convert between canonical
 *         and screen coordinates
 */
private double getScale() {
  return (double) (getWidth() - MagicNumber.GANTT_ROW_INSETS.left
      - MagicNumber.GANTT_ROW_INSETS.right)
      / (double) (rangemaximum - rangeminimum);
  
}

/**
 * Converts from canonical coordinates to screen coordinates.
 * 
 * @param value the canonical coordinate value
 * @return the screen coordinate value
 */
public double canonicalToScreen(final long value) {
  return getScale() * (value - rangeminimum)
      + MagicNumber.GANTT_ROW_INSETS.left;
  
}

/**
 * Converts from screen coordinates to canonical coordinates.
 * 
 * @param x the screen coordinate value
 * @return the canonical coordinate value
 */
public long screenToCanonical(final double x) {
  return (long) ((x - MagicNumber.GANTT_ROW_INSETS.left)
      / getScale()) + rangeminimum;
  
}

/**
 * Return the hight of each row.
 * 
 * @return the higth of each row.
 */
public int getRowHeight() {
  return MagicNumber.GANTT_ROW_INSETS.top
      + MagicNumber.GANTT_ROW_INSETS.bottom
      + MagicNumber.GANTT_ROW_HEIGHT;
  
}

/**
 * Return the Renderer of each row.
 * 
 * @return the Renderer of each row.
 */
public GanttRenderer getGanttRenderer() {
  return renderer;
  
}

/**
 * return the {@link TaskSetModel} used by this Gantt chart
 *         to extract the necessary display information
 *         from user-defined tasks
 * @return the {@link TaskSetModel} used by this Gantt chart
 *         to extract the necessary display information
 *         from user-defined tasks
 */
public TaskSetModel getTaskSetModel() {
  return taskset;
  
}

/**
 * Returns the rectangular bounds of the specified task in screen
 * coordinates.
 * 
 * @param task the task
 * @return the rectangular bounds of the specified task in screen
 *         coordinates
 */
public Rectangle2D getTaskBounds(TaskModel task) {
  int row = getTitleAt(TaskModel.getTitle(task));
  return getTaskBounds(task, row, 0, 0)
;  
}

/**
 * Returns the rectangular bounds of the specified task in screen
 * coordinates, whose position and width are modified by the additional
 * parameters.
 * 
 * @param task the task
 * @param row the new row
 * @param dstart the change in position
 * @param dwidth the change in width
 * @return the rectangular bounds of the specified task in screen
 *         coordinates, whose position and width are modified by the 
 *         additional parameters
 */
public Rectangle2D getTaskBounds(
    TaskModel task, int row, double dstart, double dwidth) {
  double start = canonicalToScreen(TaskModel.getStart(task));
  double end = canonicalToScreen(TaskModel.getEnd(task));
  double top = row*getRowHeight()
      + MagicNumber.GANTT_ROW_INSETS.top;
  
  return new Rectangle2D.Double(
      start + dstart, top, end - start + dwidth,
      MagicNumber.GANTT_ROW_HEIGHT);
}

/**
 * Returns the row at the specified vertical position in screen
 *  coordinates; or {@code null} if the position is out of bounds.
 * 
 * @param y the vertical position in screen coordinates
 *  y = ((MouseEvent) e).getPoint.getY()
 * @return the row at the specified vertical position in screen
 *  coordinates; or {@code null} if the position is out of bounds
 */
public int getRow(double y) {
  if (y < 0
      || y > getHeight())
    return -1;
  
  int row = (int)(y / getRowHeight());
  if (row >= taskset.getRowCount())
    return -1;

  return row;
  
}
/**
 * Returns the task at the specified point; or {@code null} if
 *  no task exists at that point. Pick ordering is such that
 *  selected tasks are picked first, followed by tasks in reverse
 *  order from their position in the {@link GanttModel}.
 * 
 * @param point the point
 * @return the task at the specified point; or {@code null} if
 *          no task exists at that point
 */
public TaskModel getTaskAt(Point point) {
  Rectangle2D bounds;
  TaskModel task;
  int i = getRow(point.getY());
  if (0 <= i
      && i < taskset.getRowCount()) {
    for (int j=taskset.getTaskCount(i)-1; j>=0; --j) {
      task = taskset.getTaskAt(i, j);
      bounds = getTaskBounds(task);
      if (bounds.contains(point)) {
        return task;

      }

    }
    
  }
  return null;
  
}

/**
 * Return selectedtasks.
 * 
 * @return selectedtasks
 */
public Set<TaskModel> getSelectedTasks() {
  return selectedtasks;
  
}

/**
 * Return zoom.
 * 
 * @return
 */
public double getZoom() {
  return zoom;

}

/**
 * Set zoom.
 * 
 * @param zoom
 */
//CHECKSTYLE:OFF:HiddenField
public void setZoom(double zoom) {
  this.zoom = zoom;
  
}

public long getRange() {
  return this.rangemaximum - this.rangeminimum;
      
}

public void clearSelection() {
  selectedtasks.clear();
  fireSelectionEvent();
  
}

public void toggleTaskSelection(TaskModel task) {
  if (selectedtasks.contains(task)) {
    selectedtasks.remove(task);
    
  } else {
    selectedtasks.add(task);
    
  }
  fireSelectionEvent();
  
}

public boolean isTaskSelected(TaskModel task) {
  return selectedtasks.contains(task);

}

public void selectTask(TaskModel task) {
  selectedtasks.add(task);
  fireSelectionEvent();
  
}

/**
 * Forces the Gantt chart to recompute its minimum and maximum 
 *  values and, if necessary, resize and repaint itself.
 */
public void resize() {
  long oldMinimum = rangeminimum;
  long oldMaximum = rangemaximum;
  computeRange();
  if ((oldMinimum != rangeminimum) 
      || (oldMaximum != rangemaximum)) {
    forceRevalidateAndRepaint();
    
  }
  
}

/**
 * Returns the rectangular bounds of the specified row in screen 
 * coordinates.
 * 
 * @param row the row
 * @return the rectangular bounds of the specified row in screen 
 *         coordinates
 */
public Rectangle2D getRowBounds(int row) {    
  return new Rectangle2D.Double(0.0, row*getRowHeight(), getWidth(),
      getRowHeight());
}

@Override
public void paintComponent(Graphics g) {
  Graphics2D g2 = (Graphics2D)g;
  Rectangle clip = g2.getClipBounds();
  // background
  renderer.paintBackground(g, this);
  // rows
  for (int i = taskset.getRowCount() - 1; i>=0; --i) {
    Rectangle2D bounds = getRowBounds(i);  
    if (bounds.intersects(clip)) {
      renderer.paintRow(g, this, i, bounds, false);
  
    }
  
  }
  // tasks
  for (int i = taskset.getRowCount() - 1; i>=0; --i) {
    for (int j = taskset.getTaskCount(i)-1; j >= 0; --j) {
      TaskModel task = taskset.getTaskAt(i, j);
      Rectangle2D bounds = getTaskBounds(task);
      if (bounds.intersects(clip)) {
        renderer.paintTask(
            g, this, task, bounds, isTaskSelected(task));

      }
    
    }
    
  }
  // links
  if (taskset.getTaskLinkCount() != 0) {
    for (int i = taskset.getTaskLinkCount() - 1; i >= 0; --i) {
      renderer.paintLink(g, this, taskset.getTaskLinkAt(i));
      
    }
    
  }
  
}

@Override
final public Dimension getPreferredSize() {
  int width;
  if (getParent() instanceof JViewport) {
    width = (int)(zoom * getParent().getWidth());
    
  } else {
    width = super.getPreferredSize().width;
    
  }
  int height = taskset.getRowCount() * getRowHeight();
  return new Dimension(width, height);
  
}

}
