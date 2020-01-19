package dgantt;

import java.util.EventObject;

/**
 * Event object representing changes to a {@link TaskSetModel}.
 */
public class GanttModelEvent extends EventObject {

  /**
   *=_=.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The index of the first changed row.
   */
  private final int firstRow;
  
  /**
   * The index of the last changed row.
   */
  private final int lastRow;
  
  /**
   * Class constructor for a Gantt model event indicating all rows have
   * changed.
   * 
   * @param model the changed Gantt model
   */
  public GanttModelEvent(final TaskSetModel model) {
    this(model, 0, model.getRowCount());
  }
  
  /**
   * Class constructor for a Gantt model event indicating only the specified
   * row has changed.
   * 
   * @param model the changed Gantt model
   * @param row the index of the changed row
   */
  public GanttModelEvent(final TaskSetModel model, final int row) {
    this(model, row, row);
  }
  
  /**
   * Class constructor for a Gantt model event
   *  indicating the specified rows
   *  
   *   
   *   
   *   
   *   
   *   
   *   
   *   
   *   
   *   have changed.
   * 
   * @param model the changed Gantt model
   * @param firstRow the index of the first changed row
   * @param lastRow the index of the last changed row
   */
  //CHECKSTYLE:OFF:HiddenField
  public GanttModelEvent(
      final TaskSetModel model, final int firstRow, final int lastRow) {
    super(model);
    this.firstRow = firstRow;
    this.lastRow = lastRow;
  }

  @Override
  public final TaskSetModel getSource() {
    return (TaskSetModel) super.getSource();
  }

  /**
   * Returns the index of the first changed row.
   * 
   * @return the index of the first changed row
   */
  public int getFirstRow() {
    return firstRow;
  }

  /**
   * Returns the index of the last changed row.
   * 
   * @return the index of the last changed row
   */
  public int getLastRow() {
    return lastRow;
  }

}
