package com.HamsterInside.logic;

import com.HamsterInside.TaskConfigurationProperties;
import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskGroup;
import com.HamsterInside.model.TaskGroupRepository;
import com.HamsterInside.model.TaskRepository;
import com.HamsterInside.model.projection.GroupReadModel;
import com.HamsterInside.model.projection.GroupWriteModel;




import java.util.List;
import java.util.stream.Collectors;


public class TaskGroupService
{
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository)
    {
        this.repository = repository;
        this.taskRepository = taskRepository;

    }


    public GroupReadModel createGroup(GroupWriteModel source)
    {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll()
    {
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId)
    {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
        {
            throw new IllegalStateException("Group has undone tasks> Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
