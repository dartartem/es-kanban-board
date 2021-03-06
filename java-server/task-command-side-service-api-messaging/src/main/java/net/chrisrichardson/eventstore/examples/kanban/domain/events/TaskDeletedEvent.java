package net.chrisrichardson.eventstore.examples.kanban.domain.events;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskDeletedEvent extends TaskEvent implements DetailedTaskEvent {
  private AuditEntry update;

  public TaskDeletedEvent() {
  }

  public TaskDeletedEvent(AuditEntry update) {
    this.update = update;
  }

  @Override
  public String getBoardId() {
    return null;
  }

  @Override
  public AuditEntry getUpdate() {
    return update;
  }

  public void setUpdate(AuditEntry update) {
    this.update = update;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
