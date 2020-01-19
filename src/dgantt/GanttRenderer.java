package dgantt;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * Renderer for drawing the background of a Gantt chart.  The area rendered by 
 * these to methods overlap, with {@code paintBackground} invoked prior to 
 * {@code paintRow}s.
 */
public interface GanttRenderer {
  
  /**
   * RowRenderer.
   * Renders the background of the Gantt chart.  This is called prior to 
   * any {@code paintRow} invocations.
   * 
   * @param g the graphics object used for rendering
   * @param chart the Gantt chart
   */
  void paintBackground(Graphics g, GanttChart chart);
  
  /**
   * RowRenderer.
   * Renders the background of a single row in the Gantt chart.
   * 
   * @param g the graphics object used for rendering
   * @param chart the Gantt chart
   * @param row the index of the row
   * @param bounds the rectangular bounds of the row
   * @param isTable {@code true} if this is rendering the background in a
   *        {@link GanttTable}; {@code false} if rendering the background in 
   *        a {@code GanttChart}
   */
  void paintRow(Graphics g, GanttChart chart, int row,
      Rectangle2D bounds, boolean isTable);

  /**
   * TaskRenderer.
   * Renders the specified task on the Gantt chart.
   * 
   * @param g the graphics object used for rendering
   * @param chart the Gantt chart containing the task
   * @param task the task to be rendered
   * @param bounds the rectangular bounds of the task on the Gantt chart
   * @param selected {@code true} if the specified task is currently selected;
   *        {@code false} otherwise
   */
  void paintTask(Graphics g, GanttChart chart, TaskModel task, 
      Rectangle2D bounds, boolean selected);

  /**
   * LinkRenderer.
   * Renders the specified link on the Gantt chart.
   * 
   * @param g the graphics object used for rendering
   * @param chart the Gantt chart containing the links
   * @param link the link to be rendered
   */
  void paintLink(
      Graphics g, GanttChart chart, TaskLinkModel link);
  
}
