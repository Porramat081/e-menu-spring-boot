package com.porramat081.e_menu.service.user;

import com.porramat081.e_menu.dto.UserDto;
import com.porramat081.e_menu.model.User;
import com.porramat081.e_menu.request.CreateUserRequest;
import com.porramat081.e_menu.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);

    UserDto convertToDto(User user);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

    User getAuthenticatedUser();
}
