package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
