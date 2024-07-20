package com.example.authservice.repository;

import com.example.authservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
    boolean existsByTitle(String title);
    void deleteByTitle(String title);
}
