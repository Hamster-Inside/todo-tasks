package com.HamsterInside.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Task description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne()
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    Task(){

    }
    public Task(String description, LocalDateTime deadline){
        this(description,deadline,null);
    }
    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.description = description;
        this.deadline = deadline;
        if (group != null){
            this.group = group;
        }
    }
    public LocalDateTime getDeadline()
    {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline)
    {
        this.deadline = deadline;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDone()
    {
        return done;
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }

    public void updateFrom(final Task source)
    {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;

    }


}
