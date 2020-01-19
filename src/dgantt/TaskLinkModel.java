package dgantt;

/**
 * Model storing the collection of links on a Gantt chart.
 * 
 * @author Hwrn
 */
public abstract class TaskLinkModel {

/**
 * Finish after start.
 */
public static final int START_TO_FINISH = 0;

/**
 * Start after start.
 */
public static final int START_TO_START = 1;

/**
 * Start after finish.
 */
public static final int FINISH_TO_START = 2;

/**
 * Finish after finish.
 */
public static final int FINISH_TO_FINISH = 3;

/**
 * return the task send the line.
 * 
 * @return task the line from
 */
public abstract TaskModel getLast();

/**
 * return the task get the line.
 * 
 * @return task the line to
 */
public abstract TaskModel getNext();

/**
 * the type of the link.
 * 
 * @return type of link
 */
public abstract int getType();

}
