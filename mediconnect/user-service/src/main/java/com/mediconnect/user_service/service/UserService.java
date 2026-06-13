package com.mediconnect.user_service.service;

import com.mediconnect.user_service.dto.LoginResponse;
import com.mediconnect.user_service.dto.RegisterRequest;
import com.mediconnect.user_service.dto.UserResponse;
import com.mediconnect.user_service.model.User;

public interface UserService {

   UserResponse registerUser(RegisterRequest user);

   UserResponse findByEmail(String email);

   LoginResponse loginUser(String email, String password);

}
