package org.launchcode.codingevents.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.launchcode.codingevents.data.UserRepository;
import org.launchcode.codingevents.dto.RegisterFormDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
import org.launchcode.codingevents.exception.UserRegistrationException;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        Integer userId = (Integer) session.getAttribute("user");
        if (userId == null) {
            return null;
        }

        Optional<User> user = findById(userId);

        return user.orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(RegisterFormDTO registration) {
        String password = registration.getPassword();
        String verifyPassword = registration.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            throw new UserRegistrationException("Passwords do not match");
        }

        String pwHash = passwordEncoder.encode(registration.getPassword());
        User user = new User(registration.getUsername(), pwHash);

        return userRepository.save(user);
    }

    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public User deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(userNotFoundException(id));
        userRepository.delete(user);
        return user;
    }

    public boolean validateUser(User user, String password) {
        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPwHash());
    }

    private Supplier<ResourceNotFoundException> userNotFoundException(Integer id) {
        return () -> new ResourceNotFoundException("User with id %d could not be found");
    }

}
