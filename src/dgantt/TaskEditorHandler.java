package dgantt;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.undo.UndoManager;

public class TaskEditorHandler extends MouseAdapter {

/**
 * Mode indicating no editing is occurring.
 */
public static final int NONE = 0;

/**
 * Mode indicating the start value of the task is being edited.
 */
public static final int RESIZE_START = 1;

/**
 * Mode indicating a single task is being moved.
 */
public static final int MOVE = 2;

/**
 * Mode indicating the end value of the task is being edited.
 */
public static final int RESIZE_END = 3;

/**
 * Mode indicating multiple tasks are being moved.
 */
public static final int MULTIPLE = 4;

/**
 * The last recorded mouse position, used for determine the distance
 *  the cursor has moved.
 */
private Point lastpoint;

/**
 * Current editing mode.
 *  Either NONE, RESIZE_START, RESIZE_END, MOVE or MULTIPLE.
 */
private int mode;

/**
 * The {@link GanttChart} connected to this listener.
 */
private final GanttChart chart;

/**
 * The collection of states for the tasks currently being edited.
 *  state[1] is the const gost fo  state[0]
 */
private List<TaskModel[]> states;

/**
 * The {@code UndoableEdit} if an edit is currently in progress;
 *  otherwise {@code null}.
 */
private GanttChartUndoableEdit edit;

/**
 * The undo manager where completed edits are committed.
 */
private UndoManager undomanager;

/**
 * {@code true} if the {@link GanttChart#fireChangeEvent()} method is
 * invoked as an edit is in progress; {@code false} otherwise.
 */
private boolean firechangeduringedit = true;

/**
 * Class constructor for selecting, moving and resizing tasks in the
 *  specified {@link GanttChart}.
 * 
 * @param chart
 */
//CHECKSTYLE:OFF:HiddenField
public TaskEditorHandler(GanttChart chart, UndoManager undomanager) {
  super();
  this.chart = chart;
  this.undomanager = undomanager;
  states = new ArrayList<TaskModel[]>();
  
}
/**
 * Returns {@code true} if the {@link GanttChart#fireChangeEvent()}
 *  method is invoked as an edit is in progress;
 *  {@code false} otherwise.
 * 
 * @return {@code true} if the {@link GanttChart#fireChangeEvent()}
 *         method is invoked as an edit is in progress;
 *         {@code false} otherwise
 */
public boolean isFireChangeDuringEdit() {
  return firechangeduringedit;
}

/**
 * If {@code true}, the {@link GanttChart#fireChangeEvent()} method
 *  will be invoked as an edit is in progress;
 *  {@code false} otherwise.
 * 
 * @param fireChangeDuringEdit {@code true} if the 
 *        {@link GanttChart#fireChangeEvent()} method is invoked
 *        as an edit is in progress; {@code false} otherwise
 */
public void setFireChangeDuringEdit(boolean fireChangeDuringEdit) {
  this.firechangeduringedit = fireChangeDuringEdit;
}


/**
 * Returns the editing mode for the given {@code MouseEvent}.
 * 
 * @param e the {@code MouseEvent} describing the current cursor
 *        position
 * @return the editing mode for the given {@code MouseEvent}
 */
protected int getMode(MouseEvent e) {
  if (chart.getSelectedTasks().isEmpty()) {
    return NONE;
    
  } else if (1 == chart.getSelectedTasks().size()) {
    return getMode(e, chart.getTaskAt(e.getPoint()));
  
  } else {
    return MULTIPLE;
  
  }

}

/**
 * Returns the editing mode for the given {@code MouseEvent} that is
 *  hovering over the specified task.
 * 
 * @param e the {@code MouseEvent} describing the current cursor
 *        position
 * @param task the task the mouse is currently hovering over
 * @return the editing mode for the given {@code MouseEvent}  that
 *         is hovering over the specified task
 */
protected int getMode(MouseEvent e, TaskModel task) {    
  if (Math.abs(e.getPoint().getX() - chart.canonicalToScreen(
      TaskModel.getStart(task))) <= 1.0) {
    return RESIZE_START;
    
  } else if (Math.abs(e.getPoint().getX() - chart.canonicalToScreen(
      TaskModel.getEnd(task))) <= 1.0) {
    return RESIZE_END;
    
  } else {
    return MOVE;
  
  }

}

@Override
public void mousePressed(MouseEvent e) {
  if (e.isConsumed()
      || e.getButton() != MouseEvent.BUTTON1) {
    return;
    
  }
  TaskModel selectedtask = chart.getTaskAt(e.getPoint());
  if (selectedtask == null) {
    if (!e.isControlDown()) {
      chart.clearSelection();
      
    }
    
  } else {
    if (e.isControlDown()) {
      chart.toggleTaskSelection(selectedtask);
    
    } else if (!chart.isTaskSelected(selectedtask)) {
      chart.clearSelection();
      chart.selectTask(selectedtask);
    
    }
    
  }
  chart.repaint(chart.getVisibleRect());
  if (e.isControlDown()) {
    return;
  
  }
  lastpoint = e.getPoint();  
  for (TaskModel task : chart.getSelectedTasks()) {
    TaskModel[] state = new TaskModel[2];
    state[0] = task;
    state[1] = TaskModel.copy(task);
    states.add(state);
    
  }
  mode = getMode(e);
  if (mode == NONE) {
    return;
    
  }
  edit = new GanttChartUndoableEdit(
      GanttChartUndoableEdit.MOUSE, chart, undomanager);
  for (TaskModel[] state : states) {
    edit.addEditedTask(state[0]);
    
  }
  edit.grabBeforeSnapshot();
  e.consume();
  
}

@Override
public void mouseReleased(MouseEvent e) {
  if (e.isConsumed()) {
    return;
    
  }
  if (!e.getPoint().equals(lastpoint)
      && (edit != null)) {
    edit.grabAfterSnapshot();
    edit.commit();
    
  }
  edit = null;
  lastpoint = null;
  states.clear();
  chart.resize();
  chart.repaint();
  chart.fireChangeEvent();
  
  mouseMoved(e);
  e.consume();
  
}

@Override
public void mouseDragged(MouseEvent e) {
  if (e.isConsumed()
      || lastpoint == null
      || states.isEmpty()) {
    return;

  }
  Point point = e.getPoint();
  long dx = chart.screenToCanonical(point.getX()) 
      - chart.screenToCanonical(lastpoint.getX());

  for (TaskModel[] state:states) {
    TaskModel task = state[0];
    long start = TaskModel.getStart(state[1]);
    long end = TaskModel.getEnd(state[1]);

    if (mode == RESIZE_START) {
      if (start + dx >= end - MagicNumber.MINIMUN_DURATION) {
        dx = end - start - MagicNumber.MINIMUN_DURATION;
        
      }
      start = start + dx;
      
    } else if (mode == RESIZE_END) {
      if (end + dx <= start + MagicNumber.MINIMUN_DURATION) {
        dx = start - end + MagicNumber.MINIMUN_DURATION;
        
      }
      end = end + dx;

    } else if ((mode == MOVE)
        || (mode == MULTIPLE)) {
      start = start + dx;
      end = end + dx;
      
    }
    if ((chart.canonicalToScreen(start)
            < chart.getVisibleRect().getMinX())
        && (chart.canonicalToScreen(end)
            > chart.getVisibleRect().getMaxX())) {
      // do not scroll
      
    } else {
      Rectangle2D bounds = chart.getTaskBounds(task);
      bounds.setFrame(chart.canonicalToScreen(start), bounds.getY(),
          chart.canonicalToScreen(end)
              - chart.canonicalToScreen(start), 
          bounds.getHeight());
      chart.scrollRectToVisible(bounds.getBounds());

    }
    // snap dimension to days
    Calendar cal = Calendar.getInstance();
    if (mode != RESIZE_END) {
      cal.setTimeInMillis(start);
      cal.set(Calendar.MILLISECOND, 0);
      TaskModel.setStart(task, cal.getTimeInMillis());
      
    }
    if (mode != RESIZE_START) {
      cal.setTimeInMillis(end);
      cal.set(Calendar.MILLISECOND, 0);
      TaskModel.setEnd(task, cal.getTimeInMillis());
      
    }
    if (mode == MOVE) {
      int destinationRow = chart.getRow(point.getY());
      if (destinationRow != -1) {
        int taskrow = chart.getTitleAt(TaskModel.getTitle(task));
        if (destinationRow != taskrow) {
          chart.getTaskSetModel().swapRows(destinationRow, taskrow);
          // TODO how to move? repair it
          
        }
        
      }
      
    }
            
  }
  chart.resize();
  chart.repaint();
  if (isFireChangeDuringEdit()) {
    chart.fireChangeEvent();
    
  }
  e.consume();
  
}

@Override
public void mouseMoved(MouseEvent e) {
  if (e.isConsumed()) {
    return;
    
  }
  TaskModel task = chart.getTaskAt(e.getPoint());
  if (task == null) {
    chart.setCursor(Cursor.getDefaultCursor());
    
  } else {
    int mode = getMode(e, task);
    if (mode == RESIZE_START) {
      chart.setCursor(
          Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
      
    } else if (mode == RESIZE_END) {
      chart.setCursor(
          Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

    } else if (mode == MOVE) {
      chart.setCursor(
          Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    
    } else {
      throw new RuntimeException("undefined mode");
      
    }
    e.consume();

  }

}

}
