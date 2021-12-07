package com.HamsterInside.logic;

import com.HamsterInside.model.TaskGroup;
import com.HamsterInside.model.TaskGroupRepository;
import com.HamsterInside.model.TaskRepository;
import com.HamsterInside.model.projection.GroupWriteModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskGroupServiceTest {
    TaskRepository mockTaskRepository;

    @Test
    @DisplayName("Should throw when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException(){
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturing(true);
        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");

    }



    @Test
    @DisplayName("Should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException(){
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturing(false);
        // and
        var mockRepository = mock(TaskGroupRepository.class);
        when (mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }
    @Test
    @DisplayName("Should toggle group")
    void toggleGroup_workAsExpected(){
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturing(false);
        // and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        // and
        var mockRepository = mock(TaskGroupRepository.class);
        when (mockRepository.findById(anyInt())).thenReturn(Optional.of(group));
        // system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        // when
        toTest.toggleGroup(0);
        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturing(final boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when (mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}
