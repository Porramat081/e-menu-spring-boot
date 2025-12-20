package com.porramat081.e_menu.response;

import com.porramat081.e_menu.dto.UserDto;
import com.porramat081.e_menu.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@AllArgsConstructor
@Data
public class UserDataResponse {
    private UserDto userDto;
    private Collection<Role> roles;
}
