package com.mediconnect.user_service.controller;

import com.mediconnect.user_service.dto.LoginRequest;
import com.mediconnect.user_service.dto.LoginResponse;
import com.mediconnect.user_service.dto.RegisterRequest;
import com.mediconnect.user_service.dto.UserResponse;
import com.mediconnect.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> saveUser(@RequestBody RegisterRequest registerRequest) {

        UserResponse userResponse = userService.registerUser(registerRequest);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping({"/{email}"})
    public ResponseEntity<?> findUser(@PathVariable String email) {

        try {
            UserResponse userResponse = userService.findByEmail(email);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        try {
            UserResponse userResponse = userService.findUserById(id);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
