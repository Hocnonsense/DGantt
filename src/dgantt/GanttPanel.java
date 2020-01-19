package dgantt;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A Swing component for displaying a link GanttChart},
 *  link GanttHeader} and link GanttTable}.
 *  A link JScrollPane} is created to provide scrolling,
 *  and listeners are established to ensure the three subcomponents
 *  are synchronized and correctly aligned.
 *  @author Hwrn
 */
public class GanttPanel extends JPanel {

  /**
   * =_=.
   */
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  private final GanttChart chart;

  /**
   *
   */
  private final GanttTimeAxis timeaxis;

  /**
   *
   */
  private final GanttTitleBar titlebar;

  private void layoutComponents() {
    //  divide two (and only two) Components
    final JSplitPane splitPane = new JSplitPane();
    splitPane.setResizeWeight(0);
    splitPane.setDividerLocation(
        MagicNumber.GANTTPANEL_DIVIDER_LOCATION);
    
    // title
    final JScrollPane titlebarScrollPane = new JScrollPane(titlebar,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    titlebarScrollPane.setBorder(BorderFactory.createEmptyBorder());

    final JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.add(titlebarScrollPane, BorderLayout.CENTER);
    splitPane.setLeftComponent(leftPanel);
    
    // gantt chart
    final JScrollPane chartScrollPane = new JScrollPane(chart,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    chartScrollPane.setBorder(BorderFactory.createEmptyBorder());
    chartScrollPane.setColumnHeaderView(timeaxis);
    
    final JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.add(chartScrollPane, BorderLayout.CENTER);
    
    splitPane.setRightComponent(rightPanel);
    
    // Click to reset the window. Unfinished!
    final JButton upperRightCorner = new JButton();
    upperRightCorner.setBorder(BorderFactory.createMatteBorder(
        0, 1, 0, 0, Color.GRAY));
    chartScrollPane.setCorner(
        JScrollPane.UPPER_RIGHT_CORNER, upperRightCorner);

    // The next two listeners enforce the requirement that
    //  both JScrollPanes must either be both showing
    //  or both hiding their horizontal scroll bar.
    //  This is necessary since otherwise the visible heights of the
    //  two JScrollPanes will be different, which can cause
    //  discrepancies when scrolling to the very bottom.
    chartScrollPane.getViewport().addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(final ChangeEvent e) {
            boolean visible = chart.getWidth()
                > chartScrollPane.getViewport().getWidth();
            visible |= titlebar.getWidth()
                > titlebarScrollPane.getViewport().getWidth();
            final int policy = visible
                ? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
            titlebarScrollPane.setHorizontalScrollBarPolicy(policy);
            chartScrollPane.setHorizontalScrollBarPolicy(policy);

          }

        });

    titlebarScrollPane.getViewport().addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(final ChangeEvent e) {
            boolean visible = chart.getWidth()
                > chartScrollPane.getViewport().getWidth();
            visible |= titlebar.getWidth()
                > titlebarScrollPane.getViewport().getWidth();
            final int policy = visible
                ? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
            titlebarScrollPane.setHorizontalScrollBarPolicy(policy);
            chartScrollPane.setHorizontalScrollBarPolicy(policy);

          }

        });
    
    // The following lines tell the JScrollPanes to
    //  use a backingstore, which causes all graphics
    //  to be rendered to an image buffer in memory.
    //  Then scrolling need only copy from the image buffer
    //  rather than render new image data.
    titlebarScrollPane.getViewport().setScrollMode(
        JViewport.BACKINGSTORE_SCROLL_MODE);
    chartScrollPane.getColumnHeader().setScrollMode(
        JViewport.BACKINGSTORE_SCROLL_MODE);
    chartScrollPane.getViewport().setScrollMode(
        JViewport.BACKINGSTORE_SCROLL_MODE);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
    
  }

  /**
   * stitch the chart, timebar and taskbar together.
   *  Organizes the components, adds scrollbars, and ensures
   *  the scrollbars operate in unison
   * 
   * @param chart
   * @param timeaxis
   * @param titlebar
   */
  //CHECKSTYLE:OFF:HiddenField
  public GanttPanel(final GanttChart chart,
      final GanttTimeAxis timeaxis, final GanttTitleBar titlebar) {
    super();
    this.chart = chart;
    this.timeaxis = timeaxis;
    this.titlebar = titlebar;

    layoutComponents();
  }

}
