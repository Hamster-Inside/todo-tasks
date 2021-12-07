package com.HamsterInside.logic;

import com.HamsterInside.TaskConfigurationProperties;
import com.HamsterInside.model.*;
import com.HamsterInside.model.projection.GroupReadModel;
import com.HamsterInside.model.projection.GroupTaskWriteModel;
import com.HamsterInside.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config, TaskGroupService service) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.service = service;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project ->
                {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                    }
                            ).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup);

                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
