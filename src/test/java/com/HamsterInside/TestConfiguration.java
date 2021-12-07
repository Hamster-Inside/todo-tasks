package com.HamsterInside;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskGroup;
import com.HamsterInside.model.TaskRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "adminAB", "kokos");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile("integration")
    TaskRepository testRepo() {
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById(Integer id) {
                return tasks.containsKey(id);
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return tasks.get(groupId).isDone();
            }

            @Override
            public Task save(Task entity) {
                int key = tasks.size() + 1;

                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                tasks.put(key, entity);
                return tasks.get(key);
            }

            @Override
            public Task deleteById(int id) {
                return null;
            }
        };
    }
}
