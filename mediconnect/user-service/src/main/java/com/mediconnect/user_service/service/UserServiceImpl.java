package com.mediconnect.user_service.service;

import com.mediconnect.user_service.dto.LoginResponse;
import com.mediconnect.user_service.dto.RegisterRequest;
import com.mediconnect.user_service.dto.UserResponse;
import com.mediconnect.user_service.model.User;
import com.mediconnect.user_service.repository.UserRepository;
import com.mediconnect.user_service.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse registerUser(RegisterRequest userRequest) {

        User user = userRepository.save(User.builder()
                .name(userRequest.getName())
                .phone(userRequest.getPhone())
                .role(userRequest.getRole())
                .email(userRequest.getEmail())
                .password(encoder.encode(userRequest.getPassword())).build());

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole()).build();
    }

    @Override
    public UserResponse findByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole()).build();
    }

    @Override
    public LoginResponse loginUser(String email, String password) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

        String userPassword = user.getPassword();

        if (encoder.matches(password, userPassword)) {
            return LoginResponse.builder().name(user.getName()).role(user.getRole()).token(jwtUtils.generateToken(email, user.getRole(), user.getId())).build();
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public UserResponse findUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole()).build();
    }

}
