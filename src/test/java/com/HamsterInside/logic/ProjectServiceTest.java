package com.HamsterInside.logic;

import com.HamsterInside.TaskConfigurationProperties;
import com.HamsterInside.model.*;
import com.HamsterInside.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupsExists_ThrowsIllegalStateException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig, null);
        // when + then

        // *1 sposob
        //   assertThatThrownBy(() -> {
        //      toTest.createGroup(LocalDateTime.now(), 0);
        //  }).isInstanceOf(IllegalStateException.class);

        // *2 sposob
        //  assertThatExceptionOfType(IllegalStateException.class)
        //         .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(),0));
        // *3 sposob
        //  assertThatIllegalStateException()
        //     .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(),0));
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig, null);
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
       // and
        inMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig, serviceWithInMemRepo);

        // when
        GroupReadModel result = toTest.createGroup(today, 1);

        // then
         assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(inMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo,null);
    }

    private Project projectWith(String ProjectDescription, Set<Integer> daysToDeadline) {

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(ProjectDescription);
        var daysToD = daysToDeadline.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect((Collectors.toSet()));
        when(result.getSteps()).thenReturn(daysToD);
        return result;
    }


    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private inMemoryGroupRepository inMemoryGroupRepository() {
        return new inMemoryGroupRepository();
    }

    private static class inMemoryGroupRepository implements TaskGroupRepository {

        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                map.put(entity.getId(), entity);
            } else {
                try {
                    TaskGroup.class.getDeclaredField("id").set(entity, ++index);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                map.put(index, entity);
            }
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }
}

