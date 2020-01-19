package dgantt;

public interface TaskTranslator {

/**
 * Returns the number of rows in this Gantt chart.
 *
 * @return the number of rows in this Gantt chart
 */
int getRowCount();

/**
 * Returns the number of tasks contained in given row.
 *
 * @param rowindex the row number of tasks
 * @return the number of tasks with same title
 */
int getTaskCount(int rowindex);

/**
 * Returns the task title at the specified index.
 *
 * @param rowindex the index
 * @return the task title at the specified index
 */
String getTitleAt(int rowindex);

/**
 * Return the task at the specified row and column index.
 * 
 * @param rowindex index of title or row
 * @param columnindex index of name or column
 * @return the task
 */
TaskModel getTaskAt(int rowindex, int columnindex);

/**
 * Adds a new task to this Gantt model.
 *  The default implementation is read-only.
 *
 * @param task the new task
 */
void addTask(TaskModel task);

/**
 * Removes the specified task from this Gantt model.
 *  The default implementation is read-only.
 *
 * @param task the task to remove
 */
void removeTask(TaskModel task);

/**
 * Swap two rows.
 * 
 * @param rowindex1
 * @param rowindex2
 */
void swapRows(int rowindex1, int rowindex2);

}
