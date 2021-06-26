package com.HamsterInside.model;

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
}
