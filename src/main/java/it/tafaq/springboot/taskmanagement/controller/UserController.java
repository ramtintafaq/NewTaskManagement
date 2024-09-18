package it.tafaq.springboot.taskmanagement.controller;

import it.tafaq.springboot.taskmanagement.dto.AuthenticationResponse;
import it.tafaq.springboot.taskmanagement.dto.LoginDTO;
import it.tafaq.springboot.taskmanagement.dto.RegisterDTO;
import it.tafaq.springboot.taskmanagement.entity.Users;
import it.tafaq.springboot.taskmanagement.security.JwtUtil;
import it.tafaq.springboot.taskmanagement.security.SecurityConfig;
import it.tafaq.springboot.taskmanagement.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UsersService usersService , PasswordEncoder passwordEncoder , AuthenticationManager authenticationManager , JwtUtil jwtUtil) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (usersService.existsByEmail(registerDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
        }
        else {
            Users temp =new Users(registerDTO.getUsername() , passwordEncoder.encode(registerDTO.getPassword()) , registerDTO.getEmail());
            usersService.save(temp);
            return ResponseEntity.ok("User registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            log.info("user logged in");
            return ResponseEntity.ok(new AuthenticationResponse(token));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
