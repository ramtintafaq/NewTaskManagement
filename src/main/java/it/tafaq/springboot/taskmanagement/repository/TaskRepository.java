package it.tafaq.springboot.taskmanagement.repository;

import it.tafaq.springboot.taskmanagement.entity.Task;
import it.tafaq.springboot.taskmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByUser(Users user);
    List<Task> findByUser_Username(String username);
}
