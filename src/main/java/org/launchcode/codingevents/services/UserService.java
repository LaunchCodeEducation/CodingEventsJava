package org.launchcode.codingevents.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.launchcode.codingevents.data.RoleRepository;
import org.launchcode.codingevents.data.UserRepository;
import org.launchcode.codingevents.dto.RegisterFormDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
import org.launchcode.codingevents.exception.UserRegistrationException;
import org.launchcode.codingevents.models.Privilege;
import org.launchcode.codingevents.models.Role;
import org.launchcode.codingevents.models.RoleType;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPwHash(),
                getAuthorities(user.getRoles()));
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        return findByUsername(auth.getName());
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

        if (registration.getEventOrganizer()) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByName(RoleType.ROLE_USER.toString()));
            roles.add(roleRepository.findByName(RoleType.ROLE_ORGANIZER.toString()));
            user.setRoles(roles);
        } else {
            user.setRoles(Collections.singletonList(
                roleRepository.findByName(RoleType.ROLE_USER.toString())
            ));
        }

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

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        return getGrantedAuthorities(getPrivilegesAndRoles(roles));
    }

    private List<String> getPrivilegesAndRoles(Collection<Role> roles) {
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        List<String> rolesAndPrivileges =  collection.stream()
                .map(Privilege::getName)
                .collect(Collectors.toList());
        rolesAndPrivileges.addAll(roles.stream()
            .map(Role::getName)
            .collect(Collectors.toList())
        );
        return rolesAndPrivileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Supplier<ResourceNotFoundException> userNotFoundException(Integer id) {
        return () -> new ResourceNotFoundException("User with id %d could not be found");
    }

}
