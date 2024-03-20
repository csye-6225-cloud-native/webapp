package com.cloudnative.webapi.service.impl;

import com.cloudnative.webapi.entity.User;
import com.cloudnative.webapi.model.request.CreateUserRequest;
import com.cloudnative.webapi.model.request.UpdateUserRequest;
import com.cloudnative.webapi.model.response.ApiResponse;
import com.cloudnative.webapi.model.response.UserResponse;
import com.cloudnative.webapi.repository.UserRepository;
import com.cloudnative.webapi.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> getUser(String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                logger.warn("Failed to get user: Username \"{}\" not found", username);
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

        } catch (Exception e) {
            logger.error("Failed to get user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> createUser(CreateUserRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                logger.warn("Failed to create user: Username \"{}\" already exists", request.getUsername());
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

            logger.info("User created successfully: {}", userResponse.getUsername());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);

        } catch (Exception e) {
            logger.error("Failed to create user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> updateUser(UpdateUserRequest request) {
        try {
            if (request == null) {
                logger.warn("Failed to update user: Invalid request body received");
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse(false, "Invalid request body"));
            }

            User user = userRepository.findByUsername(request.getUsername());
            if (user == null) {
                logger.warn("Failed to update user: Username \"{}\" not found", request.getUsername());
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

            logger.info("User updated successfully: {}", user.getUsername());

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (Exception e) {
            logger.error("Failed to update user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
