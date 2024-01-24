package org.launchcode.codingevents.data;

import org.launchcode.codingevents.models.Privilege;
import org.launchcode.codingevents.models.PrivilegeType;
import org.launchcode.codingevents.models.Role;
import org.launchcode.codingevents.models.RoleType;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;

@Component
@Transactional
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        Privilege createEvents = createPrivilegeIfNotFound(PrivilegeType.CREATE_EVENTS.toString());
        Privilege readEvents = createPrivilegeIfNotFound(PrivilegeType.READ_EVENTS.toString());
        Privilege deleteEvents = createPrivilegeIfNotFound(PrivilegeType.DELETE_EVENTS.toString());
        Privilege readUsers = createPrivilegeIfNotFound(PrivilegeType.READ_USERS.toString());
        Privilege updateUsers = createPrivilegeIfNotFound(PrivilegeType.UPDATE_USERS.toString());
        Privilege deleteUsers = createPrivilegeIfNotFound(PrivilegeType.DELETE_USERS.toString());

        Role adminRole = createRoleIfNotFound(RoleType.ROLE_ADMIN.toString(),
                Arrays.asList(readUsers, updateUsers, deleteUsers));
        Role organizerRole = createRoleIfNotFound(RoleType.ROLE_ORGANIZER.toString(),
                Arrays.asList(createEvents, deleteEvents));
        Role userRole = createRoleIfNotFound(RoleType.ROLE_USER.toString(),
                Arrays.asList(readEvents));

        User admin = new User("admin", passwordEncoder.encode("launchcode"),
                Arrays.asList(adminRole, organizerRole, userRole));

        createUserIfNotFound(admin);
    }

    private User createUserIfNotFound(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
        return user;
    }

    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
