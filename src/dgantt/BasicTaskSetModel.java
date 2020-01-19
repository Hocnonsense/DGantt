package dgantt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Basic {@link TaskSetModel} implenentation where each task has a 
 *  title and each title in a separate row, links are also here.
 * 
 * @author Hwrn
 */
public class BasicTaskSetModel extends TaskSetModel {

/**
 * Records title by row number.
 */
private final List<String> titles;

/**
 * Records tasks by name.
 */
private final HashMap<String, List<TaskModel>> tasks;

/**
 * Record the links.
 */
private final List<TaskLinkModel> links;

/**
 * Init titles, tasks, links.
 */
public BasicTaskSetModel() {
  super();
  titles = new ArrayList<String>();
  tasks = new HashMap<String, List<TaskModel>>();
  links = new ArrayList<TaskLinkModel>();
  
}

@Override
public final int getRowCount() {
  return titles.size();
  
}

@Override
public final int getTaskCount(final int titleindex) {
  final List<TaskModel> row = tasks.get(titles.get(titleindex));
  return row.size();
  
}

@Override
public final String getTitleAt(final int index) {
  return titles.get(index);
  
}

@Override
public final TaskModel getTaskAt(
    final int titleindex, final int nameindex) {
  return tasks.get(titles.get(titleindex)).get(nameindex);
  
}

@Override
public final void addTask(final TaskModel task) {
  final String title = TaskModel.getTitle(task);
  final int row = titles.indexOf(title);
  if (row == -1) {
    // no such title in the set
    final List<TaskModel> titlerow = new ArrayList<TaskModel>();
    titlerow.add(task);
    
    //add title and titlerow to titles and tasks
    titles.add(title);
    tasks.put(title, titlerow);
    
  } else {
    // title already in the set
    tasks.get(title).add(task);
   
  }
  
}

@Override
public final void removeTask(final TaskModel task) {
  System.out.print("???");
  final int rowindex = titles.indexOf(TaskModel.getTitle(task));
  final List<TaskModel> titlerow = tasks.get(titles.get(rowindex));
  titlerow.remove(task);
  //if this row is blank, then delete it.
  if (0 == titlerow.size()) {
    tasks.remove(titles.get(rowindex));
    titles.remove(rowindex);
    // TODO If keep a blank row?
    
  }
  // TODO Remove TaskLinks
  
} 

@Override
public final void swapRows(
    final int rowindex1, final int rowindex2) {
  final String tmp = titles.get(rowindex1);
  titles.set(rowindex1, titles.get(rowindex2));
  titles.set(rowindex2, tmp);
  refreashGanttTitleBar();
  
}

@Override
public final int getTaskLinkCount() {
  return this.links.size();
  
}

@Override
public final TaskLinkModel getTaskLinkAt(final int index) {
  return this.links.get(index);
  
}

@Override
public void addTaskLink(final TaskLinkModel tasklink) {
  // TODO Auto-generated method stub
  
}

@Override
public void removeTaskLink(final TaskLinkModel tasklink) {
  // TODO Auto-generated method stub
  
}

}
