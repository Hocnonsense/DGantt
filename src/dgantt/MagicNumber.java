package dgantt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Stroke;

/**
 * Record changeable things.
 * @author Hwrn
 */
public final class MagicNumber {

  /**
   * in {@link dgantt.Example}.drawGanttWindow().
   * 400
   */
  public static final int GANTT_WINDOW_WIDTH = 400;

  /**
   * in {@link dgantt.Example}.drawGanttWindow().
   *  400
   */
  public static final int GANTT_WINDOW_HEIGHT = 400;
  
  /**
   * in {@link dgantt.Example}.setGanttTitleBar.
   *  the title of gantt chart at left-up side
   *  Gantt
   */
  public static final String GANTT_TITLE_HEAD = "Gantt";
  
  /**
   * in {@link dgantt.GanttChart}.
   *  The width of each row
   *  20
   */
  public static final int GANTT_ROW_HEIGHT = 20;
  
  /**
   * in gantt.GanttChart.
   *  The dimensions of the empty space surrounding a task.
   */
  public static final Insets GANTT_ROW_INSETS =
      new Insets(1, 1, 1, 1);
  
  /**
   * in {@link dgantt.GanttTimeAxis};
   *    {@link dgantt.GanttTitleBar}.
   *  Gary
   */
  public static final Color BACKGROUND_COLOR_1 = Color.GRAY;
  
  /**
   * in {@link dgantt.SelectionHandler}.
   *  White
   */
  public static final Color BACKGROUND_COLOR_2 = Color.WHITE;

  
  /**
   * in {@link dgantt.GanttPanel}.layoutComponents.
   *  100
   */
  public static final int GANTTPANEL_DIVIDER_LOCATION = 100;
  
  /**
   * in {@link dgantt.SelectionHandler}.
   *  The stroke used for rendering the selection box.
   */
  public static final Stroke BOX_STROKE = new BasicStroke(1.0f, 
          BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, 
          new float[] {4.0f}, 0.0f);
  
  /**
   * in {@link handler.TaskEditHandler}.
   *  The minimum allowed duration of a task.
   *  default 0
   */
  public static final long MINIMUN_DURATION = 0;
  
  /**
   * Utility class.
   */
  private MagicNumber() {
    throw new IllegalStateException("Utility class");
  }
}
