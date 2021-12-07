package com.HamsterInside.controller;

import com.HamsterInside.model.Task;
import com.HamsterInside.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository repo;
    @Test
    void httpGet_returnsGivenTask() throws Exception{
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        //when + then
        mockMvc.perform(get("/tasks/" + id)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
