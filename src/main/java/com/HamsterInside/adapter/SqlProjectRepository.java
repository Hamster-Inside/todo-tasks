package com.HamsterInside.adapter;

import com.HamsterInside.model.Project;
import com.HamsterInside.model.ProjectRepository;
import com.HamsterInside.model.TaskGroup;
import com.HamsterInside.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer>
{

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();


}
