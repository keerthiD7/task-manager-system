package com.taskmanager.taskmanager.repository;

import com.taskmanager.taskmanager.model.Project;
import com.taskmanager.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByMembersContaining(User user);
}