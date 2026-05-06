package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.model.Project;
import com.taskmanager.taskmanager.model.Role;
import com.taskmanager.taskmanager.model.User;
import com.taskmanager.taskmanager.repository.ProjectRepository;
import com.taskmanager.taskmanager.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE PROJECT
    @PostMapping("/{userId}")
    public Project createProject(
            @PathVariable Long userId,
            @RequestBody Project project
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Creator becomes ADMIN
        user.setRole(Role.ADMIN);

        // Set admin
        project.setAdmin(user);

        // Add creator as member
        project.getMembers().add(user);

        return projectRepository.save(project);
    }

    // GET ALL PROJECTS
    @GetMapping
    public List<Project> getAllProjects() {

        return projectRepository.findAll();
    }

    // ADD MEMBER
    @PutMapping("/{projectId}/add-member/{userId}")
    public Project addMember(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        project.getMembers().add(user);

        return projectRepository.save(project);
    }

    // REMOVE MEMBER
    @PutMapping("/{projectId}/remove-member/{userId}")
    public Project removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        project.getMembers().remove(user);

        return projectRepository.save(project);
    }

    // GET PROJECTS OF USER
    @GetMapping("/user/{userId}")
    public List<Project> getUserProjects(
            @PathVariable Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return projectRepository.findByMembersContaining(user);
    }
}