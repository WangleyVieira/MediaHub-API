package com.mediahub.mediahub_api.controller;

import com.mediahub.mediahub_api.dto.request.CreateUserRequest;
import com.mediahub.mediahub_api.dto.response.UserResponse;
import com.mediahub.mediahub_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return  userService.createUser(createUserRequest);
    }
}
