package com.HamsterInside.controller;

import com.HamsterInside.logic.TaskService;
import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Controller
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(final TaskRepository repository, final TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @RequestMapping(params = {"!sort", "!page", "!size"}, method = RequestMethod.GET)
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
       // return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> {
            task.updateFrom(toUpdate);
            repository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<Task> getTask(@PathVariable int id) {
        logger.info("reading specific task");

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> postTask(@RequestBody @Valid Task createTask) {
        Task result = repository.save(createTask);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Integer> deleteTask(@PathVariable int id) {
        logger.info("deleting task -> " + id);


        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true")boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

}
