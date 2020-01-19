package dgantt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JPanel;

/**
 * Header marking years, months, weeks, days and hours
 *  along the length of a {@link GanttChart}.
 * @author Hwrn
 */
public class GanttTimeAxis extends JPanel {

  /**
   * =_=.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The {@code GanttChart} synchronized with this timeaxis.
   */
  private final GanttChart chart;

  /**
   * Class constructor for a axis making
   *  years, months, weeks, days and hours
   *  along the length of a {@code GanttChart}.
   * @param chart the {@code GanttChart} synchronized with this axis
   */
  // CHECKSTYLE:OFF:HiddenField
  public GanttTimeAxis(final GanttChart chart) {
    super();
    this.chart = chart;
    
  }

  /**
   * get Preferred Size.
   *
   * @return {@code Dimension}(width of chart, 2 * hight of font)
   */
  @Override
  public final Dimension getPreferredSize() {
    FontMetrics fm = getFontMetrics(getFont());
    return new Dimension(chart.getWidth(), 2 * fm.getHeight());
    
  }

  /**
   * Returns a normalized version of the calendar,
   * such that date attributes smaller than the
   * specified {@code stepType} are set to 0.
   *
   * @param calendar the original calendar
   * @param stepType either {@code Calendar.YEAR},
   *        {@code Calendar.MONTH},
   *        {@code Calendar.WEEK_OF_YEAR},
   *        {@code Calendar.DATE},
   *        {@code Calendar.HOUR_OF_DAY},
   *        {@code Calendar.MINUTE} or
   *        {@code Calendar.SECOND}
   * @return a normalized version of the calendar
   */
  private Calendar normalizeCalendar(
      final Calendar calendar, final int steptype) {
    Calendar result = Calendar.getInstance();
    result.clear();
            
    boolean unsupported = true;
    final int[] supportedsteptypes = {Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DATE,
        //Calendar.WEEK_OF_YEAR,
        Calendar.HOUR_OF_DAY,
        Calendar.DATE,
        Calendar.MINUTE,
        Calendar.SECOND,
    };
    
    if (steptype == Calendar.WEEK_OF_YEAR) {
      result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
      result.set(Calendar.WEEK_OF_YEAR,
          calendar.get(Calendar.WEEK_OF_YEAR));
      unsupported = false;
      
    } else {
      for (int field:supportedsteptypes) {
        result.set(field, calendar.get(field));
        if (steptype == field) {
          unsupported = false;
          break;
          
        }
        
      }
      
    }
    if (unsupported) {
      throw new IllegalStateException("unsupported stepType");
      
    } else {
      return result;
      
    }
    
  }

  /**
   * fuse {@link #renderRow} and {@link #canRenderRow}.
   * 
   * @param g
   * @param stepType
   * @param dateFormat
   * @param todraw true for renderRow and false for canRenderRow
   * @param y for renderRow
   * @param height for renderRow
   * @return
   */
  private boolean rowRenderer(final Graphics g,
      final int stepType, final DateFormat dateFormat,
      final boolean todraw,
      final double y, final double height) {
    Graphics2D g2 = (Graphics2D) g;
    Rectangle2D bounds = g2.getClipBounds();
    long minimum = chart.screenToCanonical(bounds.getMinX());
    long maximum = chart.screenToCanonical(bounds.getMaxX());
    // only for canRenderPart
    FontMetrics fm = g2.getFontMetrics();

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(minimum);
    calendar = normalizeCalendar(calendar, stepType);

    double start =
        chart.canonicalToScreen(calendar.getTimeInMillis());
    double end = 0.0;
    String text = null;

    boolean lessthenmaximun = true;
    while (lessthenmaximun) {
      text = dateFormat.format(calendar.getTime());
      calendar.add(stepType, 1);
      end = chart.canonicalToScreen(calendar.getTimeInMillis());
      
      // if from renderRow, todraw is true; else false
      if (todraw) {
        g2.setColor(MagicNumber.BACKGROUND_COLOR_1);
        g2.draw(new Line2D.Double(start, y, start, y + height));
        g2.setColor(Color.BLACK);
        TextUtilities.paintString(g2, text,
            new Rectangle2D.Double(
                start, y, end - start, height).getBounds(),
            TextUtilities.CENTER, TextUtilities.CENTER, true);
        
      } else if (end - start < fm.stringWidth(text)) {
        return false;

      }
      start = end;
      lessthenmaximun = calendar.getTimeInMillis() <= maximum;
    }
    return true;

  }
  
  /**
   * Iff the identically parameterized call to {@code renderRow}
   * can render all the text within its bounds in lower time bar.
   * check each bar from left to right.
   *
   * @param g the graphics object used for rendering
   * @param stepType either {@code Calendar.YEAR},
   *        {@code Calendar.MONTH},
   *        {@code Calendar.WEEK_OF_YEAR},
   *        {@code Calendar.DATE} or
   *        {@code Calendar.HOUR_OF_DAY}
   * @param dateFormat the date formatter
   * @return {@code true} if the identically parameterized call to
   *        {@code renderRow} can render all the text within its
   *        bounds;
   *        {@code false} otherwise
   */
  private boolean canRenderRow(final Graphics g,
      final int stepType, final DateFormat dateFormat) {
    return rowRenderer(g, stepType, dateFormat, false, 0.0, 0.0);
    
  }

  /**
   * Renders a row of the Gantt header.
   * Headers can consist of multiple rows,
   * each showing a different resolution.
   *
   * @param g the graphics object used for rendering
   * @param stepType either {@code Calendar.YEAR},
   *        {@code Calendar.MONTH}, {@code Calendar.WEEK_OF_YEAR},
   *        {@code Calendar.DATE} or {@code Calendar.HOUR_OF_DAY}
   * @param dateFormat the date formatter
   * @param y the vertical offset of the row
   * @param height the height of the row
   */
  private void renderRow(final Graphics g,
      final int stepType, final DateFormat dateFormat,
      final double y, final double height) {
    rowRenderer(g, stepType, dateFormat, true, y, height);
    
  }

  /**
   * paint words and lines
   */
  @Override
  public final void paintComponent(final Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    FontMetrics fm = getFontMetrics(getFont());

    g2.setColor(getBackground());
    g2.fill(g2.getClipBounds());

    Object[][] timeaxisformat = {
        {Calendar.MINUTE, "mm", 
          Calendar.HOUR_OF_DAY, "HH dddd", },
        {Calendar.HOUR_OF_DAY, "HH':30'",
          Calendar.DATE, "yyyy MMM dddd", },
        {Calendar.DATE, "dd",
          Calendar.MONTH, "yyyy MMM", },
        {Calendar.WEEK_OF_YEAR, "'Week' w",
          Calendar.MONTH, "yyyy MMM", },
        {Calendar.MONTH, "MMM",
          Calendar.YEAR, "yyyy", },
        
    };
    
    boolean canrender = false;
    for (Object[] timeaxis:timeaxisformat) {
      // check from small time unit.
      if (canRenderRow(g2, (int) timeaxis[0],
          new SimpleDateFormat((String) timeaxis[1]))) {
        // first line
        renderRow(g2, (int) timeaxis[2],
            new SimpleDateFormat((String) timeaxis[3]),
            0, fm.getHeight());
        g2.setColor(MagicNumber.BACKGROUND_COLOR_1);
        // separate upper and lower line.
        g2.draw(new Line2D.Double(
            0, fm.getHeight(), getWidth(), fm.getHeight()));
        // second line
        renderRow(g2, (int) timeaxis[0],
            new SimpleDateFormat((String) timeaxis[1]),
            fm.getHeight(), fm.getHeight());

        canrender = true;
        break;
      }
      
    }
    if (!canrender) {
      renderRow(g2, Calendar.YEAR,
          new SimpleDateFormat("yyyy"), 0, 2.0 * fm.getHeight());
      
    }

    g2.setColor(MagicNumber.BACKGROUND_COLOR_1);
    g2.draw(new Line2D.Double(
        0, getHeight() - 1, getWidth(), getHeight() - 1));
  }

}
