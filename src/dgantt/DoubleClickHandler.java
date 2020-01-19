package dgantt;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextField;

public class DoubleClickHandler extends MouseAdapter 
implements ActionListener, FocusListener, ComponentListener {

/**
 * The {@link GanttChart} connected to this listener.
 */
private final GanttChart chart;

/**
 * The task currently being edited; 
 * or {@code null} if no edit is currently in progress.
 */
private TaskModel task;

/**
 * The editor currently used; 
 * or {@code null} if no edit is currently in progress.
 */
private JTextField editor;

/**
 * Class constructor for detecting when a task is double-clicked
 * and displaying a text box allowing the text to be modified.
 * 
 * @param chart the {@link GanttChart} connected to this listener
 */
public DoubleClickHandler(GanttChart chart) {
  super();
  this.chart = chart;
    
}

/**
 * Invoked when a task is double-clicked.  The default implementation
 * displays a text box allowing the text to be edited.
 * 
 * @param task the task to be edited
 */
public void startEdit(TaskModel task) {
  this.task = task;
  
  int row = chart.getTitleAt(TaskModel.getTitle(task));
  Rectangle2D bounds = chart.getTaskBounds(task, row, 0, 0);
  
  editor = new JTextField(TaskModel.getName(task));
  editor.addActionListener(this);
  editor.addFocusListener(this);
  editor.setBounds(bounds.getBounds());
  editor.setOpaque(true);
  editor.setHorizontalAlignment(JTextField.CENTER);
  chart.add(editor);
  chart.addComponentListener(this);
  editor.grabFocus();
    
}

/**
 * Invoked when the editor loses focus or editing is completed.
 */
public void stopEdit() {
  chart.remove(editor);
  TaskModel.setName(task, editor.getText());
  chart.repaint(editor.getBounds());
  chart.removeComponentListener(this);
  
  editor = null;
  task = null;
  chart.fireChangeEvent();
    
}

/**
 * click outside the tast rectangular
 */
@Override
public void mousePressed(MouseEvent e) {
  if ((editor != null) 
      && (task != null)) {
    stopEdit();
    e.consume();
    
  }
  
}

@Override
public void mouseClicked(MouseEvent e) {
  if (e.isConsumed()) {
    return;
    
  }
  if ((e.getButton() == MouseEvent.BUTTON1) 
      && (e.getClickCount() == 2)) {
    TaskModel task = chart.getTaskAt(e.getPoint());
    if (task != null) {
      startEdit(task);
      e.consume();
        
    }
    
  }

}

@Override
public void mouseMoved(MouseEvent e) {
  if (task != null) {
    chart.setCursor(Cursor.getDefaultCursor());
    e.consume();
    
  }

}

@Override
public void actionPerformed(ActionEvent e) {
  if ((editor != null) 
      && (task != null)) {
    stopEdit();
  
  }

}

@Override
public void focusGained(FocusEvent e) {
    //should never be called
    
}

@Override
public void focusLost(FocusEvent e) {
  if ((editor != null) 
      && (task != null)) {
    stopEdit();
  
  }

}

@Override
public void componentHidden(ComponentEvent e) {
  if ((editor != null) 
      && (task != null)) {
    stopEdit();
  
  }

}

/**
 * unknown action. 
 */
@Override
public void componentMoved(ComponentEvent e) {
  if ((editor != null) 
      && (task != null)) {
    Rectangle2D bounds = chart.getTaskBounds(task);
    editor.setBounds(bounds.getBounds());
  
}
  
}

@Override
public void componentResized(ComponentEvent e) {
  if ((editor != null) 
      && (task != null)) {
    Rectangle2D bounds = chart.getTaskBounds(task);
    editor.setBounds(bounds.getBounds());
    
  }

}

@Override
public void componentShown(ComponentEvent e) {
  if ((editor != null) 
      && (task != null)) {
    stopEdit();
    
  }

}

}
