package com.example.authservice.service;

import com.example.authservice.dto.TaskDto;
import com.example.authservice.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDto task);

    Task updateTask(Long id, String name, Long parentId);

    void deleteTask(Task task);

    Task getTaskById(String taskId);
    Task getTaskByTitle(String title);
    List<Task> findByParentTaskId(Long parentId);
}
