package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.dto.UserDto;
import com.porramat081.e_menu.exception.AlreadyExistException;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.User;
import com.porramat081.e_menu.request.CreateUserRequest;
import com.porramat081.e_menu.request.UserUpdateRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = this.userService.getUserById(userId);
            UserDto userDto = this.userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Get User Successfully", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody CreateUserRequest request) {
        try {
            User user = this.userService.createUser(request);
            UserDto userDto = this.userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, Long userId) {
        try {
            User user = this.userService.updateUser(request, userId);
            UserDto userDto = this.userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            this.userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));

        }
    }
}
