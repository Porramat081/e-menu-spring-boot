package com.porramat081.e_menu.service.user;

import com.porramat081.e_menu.dto.UserDto;
import com.porramat081.e_menu.exception.AlreadyExistException;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.User;
import com.porramat081.e_menu.repository.UserRepository;
import com.porramat081.e_menu.request.CreateUserRequest;
import com.porramat081.e_menu.request.UserUpdateRequest;
import com.porramat081.e_menu.security.user.WebUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("user not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !this.userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return this.userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistException(request.getEmail() + " already exists"));

    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return this.userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return this.userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.findById(userId)
                .ifPresentOrElse(this.userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found");
                });
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email);
    }


}
