package it.tafaq.springboot.taskmanagement.controller;

import it.tafaq.springboot.taskmanagement.dto.TaskDTO;
import it.tafaq.springboot.taskmanagement.entity.Task;
import it.tafaq.springboot.taskmanagement.entity.Users;
import it.tafaq.springboot.taskmanagement.service.TaskService;
import it.tafaq.springboot.taskmanagement.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UsersService usersService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService , UsersService usersService) {
        this.taskService = taskService;
        this.usersService = usersService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        Users user = usersService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Task> tasks = taskService.findTaskByUsername(user.getUsername());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @PostMapping("/")
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User) authentication.getPrincipal();
        String username = userDetail.getUsername();
        Users user = usersService.findByUsername(username);
        task.setUser(user);
        taskService.saveTask(task);
        log.info("adding the task....");
        return new ResponseEntity<>("task created successfully" , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User) authentication.getPrincipal();
        String username = userDetail.getUsername();
        Users user = usersService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Task existingTask = taskService.findById(id);
        if (existingTask == null || !existingTask.getUser().getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        taskService.updateTask(existingTask);

        return new ResponseEntity<>("task updated successfully" , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetail = (User) authentication.getPrincipal();
        String username = userDetail.getUsername();
        Users user = usersService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Task existingTask = taskService.findById(id);
        if (existingTask == null || !existingTask.getUser().getUsername().equals(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskService.deleteTask(id);
        return new ResponseEntity<>("task deleted successfully" , HttpStatus.OK);
    }

}
