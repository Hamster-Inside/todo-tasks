package com.HamsterInside.controller;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;
    @Test
    void httpGet_returnsAllTasks(){
        // given
        var initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        // when
       Task[] result =  restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

       //then
        assertThat(result).hasSize(initial + 2);
    }

    @Test
    void httpPost_getPostedTask(){
        // given
        Task testTask = new Task("task1", LocalDateTime.now());
        TaskController controller = new TaskController(repo, null);

        // when
        controller.postTask(testTask);

        // then
        assertThat(controller.getTask(testTask.getId())).isNotNull();
    }



}