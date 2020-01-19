package dgantt;

/**
 * Basic model to help gantt to understend the task.
 *      String taskname
 *      String title
 *      Date start
 *      Date end
 *      enum state
 *      String detail
 * 
 * @author Hwrn
 */
public class BasicTaskModel extends TaskModel {

/**
 * State of Task.
 *  means nothing wrong.
 */
public static final int TASK_FINE = 0;

/**
 * State of Task.
 *  Finished and accepted.
 *   color: Blue
 *  Working on.
 *   color: Green
 *  Should be done but havn't.
 *   color: Red
 *  Floating and waiting to be finished.
 *   color: Yellow
 */
public static final int TASK_DELAY = 1;
//public static final int FINISHED = 0;
//public static final int DOING = 1;
//public static final int Waiting = 3;

/**
 * The name of the task.
 *  Key value
 */
private String taskname;

/**
 * The title of the task.
 *  Row
 */
private String title;

/**
 * Time to start.
 *  null for float task start
 */
private long start;

/**
 * Time to end.
 *  null for float task end
 */
private long end;

/**
 * state of task, set {@code TASK_DELAY} if delay,
 *  else {@code TASK_FINE}.
 */
private int state;

/**
 * Detail of a task.
 */
private String detail;

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setName(final String taskname) {
  this.taskname = taskname;
  
}

@Override
protected final String getName() {
  return this.taskname;
  
}

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setTitle(final String title) {
  this.title = title;
  
}

@Override
protected final String getTitle() {
  return this.title;
  
}

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setStart(final long start) {
  this.start = start;
  
}

@Override
protected final long getStart() {
  return this.start;
  
}

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setEnd(final long end) {
  this.end = end;
  
}

@Override
protected final long getEnd() {
  return this.end;
  
}

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setState(final int state) {
  this.state = state;
  
}

@Override
protected final int getState() {
  return this.state;
  
}

@Override
//CHECKSTYLE:OFF:HiddenField
protected final void setDetail(final String detail) {
  this.detail = detail;
  
}

@Override
protected final String getDetail() {
  return this.detail;
  
}

@Override
protected TaskModel copy() {
  BasicTaskModel copy = new BasicTaskModel();
  copy.taskname = this.taskname;
  copy.title = this.title;
  copy.start = this.start;
  copy.end = this.end;
  copy.state = this.state;
  copy.detail = this.detail;
  return copy;
}

}
