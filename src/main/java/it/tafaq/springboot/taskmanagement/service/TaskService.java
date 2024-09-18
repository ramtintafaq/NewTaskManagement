package it.tafaq.springboot.taskmanagement.service;

import it.tafaq.springboot.taskmanagement.entity.Task;
import it.tafaq.springboot.taskmanagement.entity.Users;
import it.tafaq.springboot.taskmanagement.repository.TaskRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findTasksByUser(Users user) {
        return taskRepository.findTasksByUser(user);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findTaskByUsername(String username) {
        return taskRepository.findByUser_Username(username);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}
