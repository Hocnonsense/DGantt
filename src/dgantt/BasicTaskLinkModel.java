package dgantt;

public class BasicTaskLinkModel extends TaskLinkModel {

/**
 * Line from.
 */
private final TaskModel last;

/**
 * Line to.
 */
private final TaskModel next;

/**
 * Line type.
 * only type can be changed.
 */
private int type;

/**
 * Set link.
 * 
 * @param last
 * @param next
 * @param type
 */
//CHECKSTYLE:OFF:HiddenField
public BasicTaskLinkModel(
    final TaskModel last, final TaskModel next, final int type) {
  this.last = last;
  this.next = next;
  this.type = type;
  
}

@Override
public final TaskModel getLast() {
  return this.last;
  
}

@Override
public final TaskModel getNext() {
  return this.next;
  
}

@Override
public final int getType() {
  return this.type;
  
}

/**
 * Set type of the line.
 * @param type
 */
public final void setType(final int type) {
  this.type = type;
}

}
