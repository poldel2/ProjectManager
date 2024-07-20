package com.example.authservice.service.impl;

import com.example.authservice.dto.TaskDto;
import com.example.authservice.model.Task;
import com.example.authservice.repository.TaskRepository;
import com.example.authservice.service.TaskService;

import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());

        Task parentTask = taskRepository.findByTitle(taskDto.getParentTitle()); //TODO::доделать поиск сBерхтаска
        return task;
    }


    @Override
    public Task updateTask(Long id, String name, Long parentId) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(name);
            if (parentId != null) {
                Task parentTask = taskRepository.findById(parentId).orElse(null);
                task.setParentTask(parentTask);
            } else {
                task.setParentTask(null);
            }
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.deleteByTitle(task.getTitle());
    }

    @Override
    public Task getTaskById(String taskId) {
        return null;
    }

    public Optional<Task> getTask(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Override
    public List<Task> findByParentTaskId(Long parentId) {
        return List.of();
    }

}
