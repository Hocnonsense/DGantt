package dgantt;

/**
 * Interface for TaskSetModel.
 *  Define Works of Links
 * 
 * @author Hwrn
 */
public interface LinkTranslator {

/**
 * Returns the number of links.
 * 
 * @return the number of links
 */
int getTaskLinkCount();

/**
 * Returns the link at the specified index.
 * 
 * @param index the index
 * @return the link at the specified index
 */
TaskLinkModel getTaskLinkAt(int index);

/**
 * Adds a new link to the Gantt chart.
 * 
 * @param tasklink the new link
 */
void addTaskLink(TaskLinkModel tasklink);

/**
 * Removes the specified link from the Gantt chart.
 * 
 * @param tasklink the link to remove
 */
void removeTaskLink(TaskLinkModel tasklink);

}
