package dgantt;

import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Model storing the collection of tasks displayed in the Gantt
 *  chart.
 */
public abstract class TaskSetModel implements
TaskTranslator, LinkTranslator {

/**
 * Refreash when title changed.
 */
private AbstractTableModel titlebarmodel;

/**
 * The {@link GanttModelListener}s registered to receive
 *  Gantt model changed events from this Gantt model.
 */
private final List<GanttModelListener> listeners;

/**
 * Default constructor for a Gantt model.
 */
public TaskSetModel() {
  super();
  listeners = new Vector<GanttModelListener>();

}

/**
 * Registers the specified {@code GanttModelListener}
 *  to receive events from this Gantt model.
 *
 * @param listener the {@code GanttModelListener}
 *  to receive events from this Gantt model
 */
public void addGanttModelListener(
    final GanttModelListener listener) {
  listeners.add(listener);

}

/**
 * Unregisters the specified {@code GanttModelListener}
 *  to no longer receive events from this Gantt model.
 *
 * @param listener the {@code GanttModelListener}
 *  to no longer receive events from this Gantt model
 */
public void removeGanttModelListener(
    final GanttModelListener listener) {
  listeners.remove(listener);
  
}

/**
 * Notifies all registered {@code GanttModelListener}s
 *  that this model has been changed.
 */
public void fireGanttModelChanged() {
  fireGanttModelChanged(new GanttModelEvent(this));
  
}

/**
 * Notifies all registered {@code GanttModelListener}s that the
 *  specified row has been changed.
 *
 * @param row the index of the changed row
 */
public void fireGanttModelChanged(final int row) {
  fireGanttModelChanged(new GanttModelEvent(this, row));
  
}

/**
 * Notifies all registered {@code GanttModelListener}s that the
 *  specified rows have been changed.
 *
 * @param firstRow the index of the first changed row
 * @param lastRow the index of the last changed row
 */
public void fireGanttModelChanged(
    final int firstRow, final int lastRow) {
  fireGanttModelChanged(new GanttModelEvent(
      this, firstRow, lastRow));
  
}

/**
 * Invokes the
 *  {@link GanttModelListener#ganttModelChanged(GanttModelEvent)}
 *  method on all registered {@code GanttModelListener}s.
 *
 * @param event the event
 */
public void fireGanttModelChanged(final GanttModelEvent event) {
  for (GanttModelListener listener : listeners) {
    listener.ganttModelChanged(event);

  }

}

/**
 * Set titlebar into this set and fire it ontime.
 * 
 * @param titlebar
 */
//CHECKSTYLE:OFF:HiddenField
public void connectGanttTitleBar(final JTable titlebar) {
  this.titlebarmodel = (AbstractTableModel) titlebar.getModel();
  
}

/**
 * Refreash titlebar when title changed
 */
protected void refreashGanttTitleBar() {
  titlebarmodel.fireTableDataChanged();
  
}

}
