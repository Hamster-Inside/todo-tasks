package com.HamsterInside.logic;

import com.HamsterInside.TaskConfigurationProperties;
import com.HamsterInside.model.ProjectRepository;
import com.HamsterInside.model.TaskGroupRepository;
import com.HamsterInside.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(final ProjectRepository repository,
                                  final TaskGroupRepository taskGroupRepository,
                                  final TaskConfigurationProperties config)
    {
        return new ProjectService(repository, taskGroupRepository, config);
    }
    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository repository,
                                      final TaskRepository taskRepository)
    {
        return new TaskGroupService(repository,taskRepository);
    }
}
