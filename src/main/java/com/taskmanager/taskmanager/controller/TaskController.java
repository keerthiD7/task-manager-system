package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.model.Status;
import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.model.User;
import com.taskmanager.taskmanager.repository.TaskRepository;
import com.taskmanager.taskmanager.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE TASK
    @PostMapping
    public Task createTask(@RequestBody Task task) {

        return taskRepository.save(task);
    }

    // GET ALL TASKS
    @GetMapping
    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    // UPDATE STATUS
    @PutMapping("/{id}")
    public Task updateTaskStatus(
            @PathVariable Long id,
            @RequestBody Task updatedTask
    ) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        task.setStatus(updatedTask.getStatus());

        return taskRepository.save(task);
    }

    // ASSIGN TASK
    @PutMapping("/{taskId}/assign/{userId}")
    public Task assignTask(
            @PathVariable Long taskId,
            @PathVariable Long userId
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        task.setAssignedTo(user);

        return taskRepository.save(task);
    }

    // USER TASKS
    @GetMapping("/user/{userId}")
    public List<Task> getUserTasks(
            @PathVariable Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return taskRepository.findByAssignedTo(user);
    }
}