package com.HamsterInside.controller;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Controller
class TaskController
{
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository)
    {
        this.repository = repository;
    }

    @RequestMapping(value = "/tasks", params = {"!sort", "!page", "!size"}, method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks()
    {
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page)
    {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate)
    {
        if (!repository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
    ResponseEntity<Task> getTask(@PathVariable int id)
    {
        logger.info("reading specific task");

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    ResponseEntity<Task> postTask(@RequestBody @Valid Task createTask){
        Task result = repository.save(createTask);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Integer> deleteTask(@PathVariable int id){
        logger.info("deleting task -> " + id);



        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
