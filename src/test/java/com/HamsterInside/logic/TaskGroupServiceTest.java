package com.HamsterInside.logic;

import com.HamsterInside.model.TaskGroupRepository;
import com.HamsterInside.model.TaskRepository;
import com.HamsterInside.model.projection.GroupWriteModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskGroupServiceTest {

    @Test
    @DisplayName("try to change done when is false throwing illegal state exception")
    void changeDone_whenDoneIsFalse_And_GroupId_ThrowIllegalStateException(){
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        var exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("Group has undone tasks");
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupServiceRepository = mock(TaskGroupRepository.class);
        when(mockGroupServiceRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupServiceRepository;
    }

}
