package com.HamsterInside.model.projection;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel
{
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {return description;}

    public void setDescription(final String description) {this.description = description;}

    public LocalDateTime getDeadline() {return deadline;}

    public void setDeadline(final LocalDateTime deadline) {this.deadline = deadline;}

    public Task toTask(TaskGroup group) {return new Task(description, deadline, group);}
}
