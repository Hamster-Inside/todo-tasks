package com.HamsterInside.adapter;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer>
{
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from TASKS where ID=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);


}
