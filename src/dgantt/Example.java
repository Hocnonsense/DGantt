package dgantt;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

/**
 * Basic Window for Gantt.
 *  - [Hocnonsense/DGantt](https://github.com/Hocnonsense/DGantt)
 *   - A simple Gantt chart based on DGantt.  It is reconfigured.
 * 
 * @author Hwrn
 */
public final class Example {

/**
 * Init window.
 * Version 0.0.1
 */
public Example() {
  setLookAndFeel();
  
  final TaskSetModel taskset = new BasicTaskSetModel();
  setTaskSet(taskset);
  addTaskLink(taskset);
  
  final GanttRenderer renderer = new DefaultGanttRenderer();

  // chart, the main showing place
  final GanttChart ganttchart = new GanttChart(taskset, renderer);

  // timebar, to show the time
  final GanttTimeAxis gantttimeaxis =
      new GanttTimeAxis(ganttchart);

  // titlebar, to show the task or resourse occupy
  final GanttTitleBar gantttitlebar =
      new GanttTitleBar(ganttchart, gantttimeaxis);

  // add GanttChartHandler
  addGanttChartHandler(ganttchart, gantttitlebar);

  final GanttPanel ganttpanel = new GanttPanel(
      ganttchart, gantttimeaxis, gantttitlebar);

  // show details of tasks
  final JPanel bottompanel = drawBottomPanel();

  // lastly, create and display the window
  final JFrame ganttwindow =
      drawGanttWindow(ganttpanel, bottompanel);
  ganttwindow.setVisible(true);
  
}

public static void Main()

/**
 * Set system style window.
 */
private void setLookAndFeel() {
  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

  } catch (Exception e) { }

}

/**
 * Set TaskSet.
 * 
 * @param taskset
 */
//CHECKSTYLE:OFF:MagicNumber
private void setTaskSet(final TaskSetModel taskset) {
  final TaskModel task1 = new BasicTaskModel();
  TaskModel.setName(task1, "task 1");
  final Calendar calendar = Calendar.getInstance();
  TaskModel.setStart(task1, calendar.getTime().getTime());
  calendar.add(Calendar.MINUTE, 2);
  TaskModel.setEnd(task1, calendar.getTime().getTime());
  TaskModel.setTitle(task1, "default");
  TaskModel.setState(task1, BasicTaskModel.TASK_FINE);
  TaskModel.setDetail(task1, "None");
  taskset.addTask(task1);
  final TaskModel task2 = new BasicTaskModel();
  TaskModel.setName(task2, "task 2");
  TaskModel.setStart(task2, calendar.getTime().getTime());
  calendar.add(Calendar.MINUTE, 2);
  TaskModel.setEnd(task2, calendar.getTime().getTime());
  TaskModel.setTitle(task2, "default");
  TaskModel.setState(task2, BasicTaskModel.TASK_FINE);
  TaskModel.setDetail(task2, "None");
  taskset.addTask(task2);
  final TaskModel task3 = new BasicTaskModel();
  TaskModel.setName(task3, "task 3");
  TaskModel.setStart(task3, calendar.getTime().getTime());
  calendar.add(Calendar.SECOND, 50);
  TaskModel.setEnd(task3, calendar.getTime().getTime());
  TaskModel.setTitle(task3, "another");
  TaskModel.setState(task3, BasicTaskModel.TASK_DELAY);
  TaskModel.setDetail(task3, "None");
  taskset.addTask(task3);
  
}

/**
 * Set TaskLink.
 * 
 * @param taskset
 */
private void addTaskLink(final TaskSetModel taskset) {
  //TODO add links to taskset
  
}

private void addGanttChartHandler(
    final GanttChart ganttchart, GanttTitleBar gantttitlebar) {
//create a handler for box selection using the left-mouse button
  final SelectionHandler boxSelectionHandler =
      new SelectionHandler(ganttchart);
  ganttchart.addMouseListener(boxSelectionHandler);
  ganttchart.addMouseMotionListener(boxSelectionHandler);

  // create handler for manipulating links
  final LinkEditorHandler linkHandler =
      new LinkEditorHandler(ganttchart);
  ganttchart.addMouseListener(linkHandler);
  ganttchart.addMouseMotionListener(linkHandler);

  // create a handler for zooming using the right-mouse button
  final ZoomHandler zoomHandler = new ZoomHandler(ganttchart);
  ganttchart.addMouseListener(zoomHandler);
  ganttchart.addMouseMotionListener(zoomHandler);

  // create a handler for double-clicking a task to change its text
  final DoubleClickHandler doubleClickHandler =
      new DoubleClickHandler(ganttchart);
  ganttchart.addMouseListener(doubleClickHandler);
  ganttchart.addMouseMotionListener(doubleClickHandler);

  // create a handler for moving, resizing and selecting tasks
  final TaskEditorHandler taskEditorHandler =
      new TaskEditorHandler(ganttchart, new UndoManager());
  ganttchart.addMouseListener(taskEditorHandler);
  ganttchart.addMouseMotionListener(taskEditorHandler);

}

/**
 * Draw text area to show detail for task in gantt chart.
 * 
 * @return bottompanel
 */
private JPanel drawBottomPanel() {
  final JPanel bottompanel = new JPanel(new BorderLayout());
  
  final JTextArea taskdetail =
      new JTextArea("Details are showing here...");
  taskdetail.setLineWrap(true);
  //taskdetail.setEditable(false);
  bottompanel.add(taskdetail);
  
  return bottompanel;
  
}

/**
 * Gantt window setting.
 *
 * @param ganttpanel ganttpanel
 * @param bottompanel bottompanel
 * @return ganttwindow
 */
private JFrame drawGanttWindow(final GanttPanel ganttpanel,
    final JPanel bottompanel) {
  final JFrame ganttwindow = new JFrame("gantt version 0.0.1");
  ganttwindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  ganttwindow.setSize(MagicNumber.GANTT_WINDOW_WIDTH,
      MagicNumber.GANTT_WINDOW_HEIGHT);

  ganttwindow.getContentPane().add(ganttpanel, BorderLayout.CENTER);
  ganttwindow.getContentPane().add(bottompanel, BorderLayout.SOUTH);

  return ganttwindow;

}

}
