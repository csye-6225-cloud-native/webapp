package com.cloudnative.webapi.service.impl;

import com.cloudnative.webapi.entity.User;
import com.cloudnative.webapi.model.request.CreateUserRequest;
import com.cloudnative.webapi.model.request.UpdateUserRequest;
import com.cloudnative.webapi.model.response.ApiResponse;
import com.cloudnative.webapi.model.response.UserResponse;
import com.cloudnative.webapi.repository.UserRepository;
import com.cloudnative.webapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found"));
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setAccountCreated(user.getAccountCreated());
        userResponse.setAccountUpdated(user.getAccountUpdated());

        return ResponseEntity.ok(userResponse);
    }

    public ResponseEntity<?> createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Username already exists"));
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstname(),
                request.getLastname()
        );

        userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setAccountCreated(user.getAccountCreated());
        userResponse.setAccountUpdated(user.getAccountUpdated());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }

    public ResponseEntity<?> updateUser(UpdateUserRequest request) {
        if (request == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Invalid request body"));
        }

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found"));
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFirstname() != null && !request.getFirstname().trim().isEmpty()) {
            user.setFirstname(request.getFirstname());
        }

        if (request.getLastname() != null && !request.getLastname().trim().isEmpty()) {
            user.setLastname(request.getLastname());
        }

        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
