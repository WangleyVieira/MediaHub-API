package com.mediahub.mediahub_api.service;

import com.mediahub.mediahub_api.dto.request.CreateUserRequest;
import com.mediahub.mediahub_api.dto.response.UserResponse;
import com.mediahub.mediahub_api.model.User;
import com.mediahub.mediahub_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest createUserRequest) {

        if (userRepository.existsByEmail(createUserRequest.email())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(createUserRequest.name());
        user.setEmail(createUserRequest.email());
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getCreatedAt(),
            savedUser.getUpdatedAt()
        );
    }

}
