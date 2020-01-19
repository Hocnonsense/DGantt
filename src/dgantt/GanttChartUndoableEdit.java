package dgantt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * An {@link UndoableEdit} for recording changes made to a
 *  {@link GanttChart} and providing the means to undo such changes.
 *  Four steps are required: 
 *      1) add edited tasks;
 *      2) grab a snapshot of the before states;
 *      3) grab a snapshot of the after states; and 
 *      4) commit the changes to the {@link UndoManager}.
 */
public class GanttChartUndoableEdit extends AbstractUndoableEdit {

/**
 * Constant for identifying edits made using the keyboard.
 */
public static final int KEYBOARD = 0;

/**
 * Constant for identifying edits made using the mouse.
 */
public static final int MOUSE = 1;

/**
 * =_=.
 */
private static final long serialVersionUID = 1L;

/**
 * The type of edit.  Either KEYBOARD or MOUSE.
 */
private int type;

/**
 * The {@code UndoManager} where edits are saved once committed.
 */
private final UndoManager undoManager;

/**
 * The {@code GanttChart} producing the edits.
 */
private final GanttChart chart;

/**
 * The collection of currently edited tasks.
 *  [editedTask, beforeState, afterState]
 */
private List<TaskModel[]> editedTasks;

/**
 * Class constructor for a new {@code UndoableEdit} to a
 *  {@code GanttChart}.
 * 
 * @param type the type of edit; either KEYBOARD or MOUSE
 * @param chart the {@code GanttChart} producing the edit
 * @param undoManager the {@code UndoManager} where the edit
 *         is saved once committed
 */
//CHECKSTYLE:OFF:HiddenField
public GanttChartUndoableEdit(final int type, final GanttChart chart,
    final UndoManager undoManager) {
  super();
  this.type = type;
  this.chart = chart;
  this.undoManager = undoManager;
  editedTasks = new ArrayList<TaskModel[]>();
  
}

@Override
public final void undo() {
  for (TaskModel[] state : editedTasks) {
    TaskModel.setName(state[0], TaskModel.getName(state[2]));
    TaskModel.setTitle(state[0], TaskModel.getTitle(state[1]));
    TaskModel.setStart(state[0], TaskModel.getStart(state[1]));
    TaskModel.setEnd(state[0], TaskModel.getEnd(state[1]));
    TaskModel.setState(state[0], TaskModel.getState(state[1]));
    TaskModel.setDetail(state[0], TaskModel.getDetail(state[1]));
    
  }
  chart.fireChangeEvent();
  chart.repaint();
  
}

@Override
public final void redo() {
  for (TaskModel[] state : editedTasks) {
    TaskModel.setName(state[0], TaskModel.getName(state[2]));
    TaskModel.setTitle(state[0], TaskModel.getTitle(state[2]));
    TaskModel.setStart(state[0], TaskModel.getStart(state[2]));
    TaskModel.setEnd(state[0], TaskModel.getEnd(state[2]));
    TaskModel.setState(state[0], TaskModel.getState(state[2]));
    TaskModel.setDetail(state[0], TaskModel.getDetail(state[2]));
    
  }
  chart.fireChangeEvent();
  chart.repaint();
  
}

@Override
public final boolean canRedo() {
  return true;
  
}

/**
 * Adds the specified task to this {@code UndoableEdit}.
 *  Only tasks added to this {@code UndoableEdit} will be recorded.
 * 
 * @param task the task being edited
 */
public void addEditedTask(final TaskModel task) {
  final TaskModel[] state = new TaskModel[3];
  state[0] = task;
  editedTasks.add(state);
  
}

/**
 * Records the before state of all tasks.
 */
public void grabBeforeSnapshot() {
  for (TaskModel[] state : editedTasks) {
    state[1] = TaskModel.copy(state[0]);
    
  }
  
}

/**
 * Records the after state of all tasks.
 */
public void grabAfterSnapshot() {
  for (TaskModel[] state : editedTasks) {
    state[2] = TaskModel.copy(state[0]);
  
  }
  
}

@Override
public final boolean addEdit(final UndoableEdit edit) {
  if (edit instanceof GanttChartUndoableEdit) {
    final GanttChartUndoableEdit newedit =
        (GanttChartUndoableEdit) edit;
  
    if ((type == KEYBOARD) && (newedit.type == KEYBOARD)) {
      if (editedTasks.equals(newedit.editedTasks)) {
        // TODO how to add edit and mergewq
        return true;
    
      }
  
    }
  
  }
  return false;
  
}

/**
 * Saves this {@code UndoableEdit} to the {@code UndoManater}.
 */
public void commit() {
  undoManager.addEdit(this);
  editedTasks.clear();
  
}

}
