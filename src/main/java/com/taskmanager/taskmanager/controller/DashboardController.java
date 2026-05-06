package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private TaskRepository taskRepository;

    // TOTAL TASKS
    @GetMapping("/total-tasks")
    public long totalTasks() {

        return taskRepository.count();
    }

    // TASKS BY STATUS
    @GetMapping("/task-status")
    public Map<String, Long> taskStatus() {

        List<Task> tasks = taskRepository.findAll();

        long todo = tasks.stream()
                .filter(task ->
                        "TODO".equals(task.getStatus().toString()))
                .count();

        long inProgress = tasks.stream()
                .filter(task ->
                        "IN_PROGRESS".equals(task.getStatus().toString()))
                .count();

        long done = tasks.stream()
                .filter(task ->
                        "DONE".equals(task.getStatus().toString()))
                .count();

        Map<String, Long> response = new HashMap<>();

        response.put("TODO", todo);
        response.put("IN_PROGRESS", inProgress);
        response.put("DONE", done);

        return response;
    }

    // OVERDUE TASKS
    @GetMapping("/overdue")
    public List<Task> overdueTasks() {

        return taskRepository.findAll()
                .stream()
                .filter(task ->
                        task.getDueDate() != null &&
                                task.getDueDate().isBefore(LocalDate.now()) &&
                                !"DONE".equals(task.getStatus().toString()))
                .toList();
    }

    // TASKS PER USER
    @GetMapping("/tasks-per-user")
    public Map<String, Long> tasksPerUser() {

        List<Task> tasks = taskRepository.findAll();

        Map<String, Long> response = new HashMap<>();

        tasks.stream()
                .filter(task -> task.getAssignedTo() != null)
                .forEach(task -> {

                    String userName =
                            task.getAssignedTo().getName();

                    response.put(
                            userName,
                            response.getOrDefault(userName, 0L) + 1
                    );
                });

        return response;
    }
}