package dgantt;

import java.util.EventObject;

/**
 * Event object representing selection events in a Gantt chart.
 *  The selected tasks can be retrieved using
 *  {@code e.getSource().getSelectedTasks()}.
 */
public class GanttSelectionEvent extends EventObject {

  /**
   * =_=.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Class constructor for a new Gantt selection event
   *  originating from the specified source.
   * 
   * @param source the Gantt chart from which this event originated
   */
  public GanttSelectionEvent(final GanttChart source) {
    super(source);
    
  }

  @Override
  public final GanttChart getSource() {
    return (GanttChart) super.getSource();
    
  }

}
