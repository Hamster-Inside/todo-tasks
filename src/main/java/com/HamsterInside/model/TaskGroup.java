package com.HamsterInside.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private boolean done;

    @Embedded
    private Audit audit = new Audit();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")

    private Set<Task> tasks;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    private Project project;

    public TaskGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {return project;}

    public void setProject(final Project project) {
        this.project = project;
    }
}
