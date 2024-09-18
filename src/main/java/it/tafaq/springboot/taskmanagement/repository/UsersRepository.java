package it.tafaq.springboot.taskmanagement.repository;

import it.tafaq.springboot.taskmanagement.entity.Task;
import it.tafaq.springboot.taskmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users , Long> {
    Users findByUsername(String username);
    Users findByEmail(String email);
}
