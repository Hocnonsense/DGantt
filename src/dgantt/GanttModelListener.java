package dgantt;

/**
 * Listener interface for receiving notifications when
 *  a {@link TaskSetModel} is changed.
 */
public interface GanttModelListener {

  /**
   * Invoked by a Gantt chart whenever its underlying
   *  {@code GanttModel} is changed.
   *
   * @param event the Gantt model event
   */
  void ganttModelChanged(GanttModelEvent event);

}
