package dgantt;

import java.util.EventListener;

/**
 * Listener interface for receiving notifications whenever
 *  the selected tasks in a Gantt chart are changed.
 */
public interface GanttSelectionListener extends EventListener {

/**
 * Invoked by a Gantt chart whenever the selected tasks in a Gantt
 *  chart are changed.
 * 
 * @param e the selection event
 */
void valueChanged(GanttSelectionEvent e);

}
