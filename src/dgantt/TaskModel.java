package dgantt;

/**
 * TaskModel to extract the necessary Gantt chart details from
 *  a user-defined task.
 *  The units of the start and end values are arbitrary.
 *  However, if using the built-in support for dates,
 *  the start and end values must be the date in milliseconds
 *  (see {@link Date#getTime}).
 *  includes abstract and static, set and get methods for:
 *      String taskname
 *      String title
 *      Date start
 *      Date end
 *      enum state
 *      String detail
 */
public abstract class TaskModel {

  /**
   * Default constructor.
   */
  public TaskModel() {
    super();

  }

  /**
   * Set name.
   * 
   * @param name
   */
  protected abstract void setName(String name);
  
  /**
   * Set name.
   * 
   * @param task
   * @param name
   */
  public static void setName(
      final TaskModel task, final String name) {
    task.setName(name);
    
  }

  /**
   * Returns the name of the specified task. 
   * 
   * @return the name of the specified task
   */
  protected abstract String getName();
  
  /**
   * Returns the name of the specified task. 
   * 
   * @param task the task
   * @return the name of the specified task
   */
  public static String getName(final TaskModel task) {
    return task.getName();
    
  }
  
  /**
   * set title.
   * 
   * @param title
   */
  protected abstract void setTitle(String title);
  
  /**
   * set title.
   * 
   * @param task
   * @param title
   */
  public static void setTitle(
      final TaskModel task, final String title) {
    task.setTitle(title);
    
  }
  
  /**
   * Retures the title of the specified task.
   * 
   * @return the title of the specified task
   */
  protected abstract String getTitle();

  /**
   * Retures the title of the specified task.
   * 
   * @param task
   * @return the title of the specified task
   */
  public static String getTitle(final TaskModel task) {
    return task.getTitle();
  }
  
  /**
   * Set start time.
   * 
   * @param start
   */
  protected abstract void setStart(long start);
  
  /**
   * Set start time.
   * 
   * @param task
   * @param start
   */
  public static void setStart(
      final TaskModel task, final long start) {
    task.setStart(start);
    
  }

  /**
   * Returns the start value of the specified task.
   * 
   * @return the start value of the specified task
   */
  protected abstract long getStart();

  /**
   * Returns the start value of the specified task.
   * 
   * @param task
   * @return the start value of the specified task
   */
  public static long getStart(final TaskModel task) {
    return task.getStart();
    
  }

  /**
   * Set end time.
   * 
   * @param end
   */
  protected abstract void setEnd(long end);

  /**
   * Set end time.
   * 
   * @param task
   * @param end
   */
  public static void setEnd(
      final TaskModel task, final long end) {
    task.setEnd(end);
    
  }

  /**
   * Returns the end value for the specified task.
   * 
   * @return the end value of the specified task
   */
  protected abstract long getEnd();
  
  /**
   * Returns the end value for the specified task.
   * 
   * @param task
   * @return the end value of the specified task
   */
  public static long getEnd(final TaskModel task) {
    return task.getEnd();
    
  }
  
  /**
   * Set task state.
   * 
   * @param state
   */
  protected abstract void setState(int state);
  
  /**
   * Set task state.
   * 
   * @param task
   * @param state
   */
  public static void setState(
      final TaskModel task, final int state) {
    task.setState(state);
    
  }
  
  /**
   * Return the state of the speicfied task.
   * 
   * @return the state of the speicfied task
   */
  protected abstract int getState();
  
  /**
   * Return the state of the speicfied task.
   * 
   * @param task
   * @return the state of the speicfied task
   */
  public static int getState(final TaskModel task) {
    return task.getState();
    
  }

  /**
   * set task detail.
   * 
   * @param detail
   */
  protected abstract void setDetail(String detail);

  /**
   * set task detail.
   * 
   * @param task
   * @param detail
   */
  public static void setDetail(
      final TaskModel task, final String detail) {
    task.setDetail(detail);
    
  }
  
  /**
   * Return the detail of the speicfied task.
   * 
   * @return the detail of the speicfied task
   */
  protected abstract String getDetail();
  
  /**
   * Return the detail of the speicfied task.
   * 
   * @param task
   * @return the detail of the speicfied task
   */
  public static String getDetail(final TaskModel task) {
    return task.getDetail();
    
  }

  public static TaskModel copy(TaskModel task) {
    return task.copy();
  }
  
  protected abstract TaskModel copy();
  
}
