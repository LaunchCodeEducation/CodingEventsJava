package org.launchcode.codingevents.services;

import java.util.List;
import java.util.Optional;

import org.launchcode.codingevents.dto.RegisterFormDTO;
import org.launchcode.codingevents.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username);
    User getCurrentUser();
    User save(RegisterFormDTO registration);
    Optional<User> findById(Integer id);
    List<User> findAll();
    User deleteUser(Integer id);
}
