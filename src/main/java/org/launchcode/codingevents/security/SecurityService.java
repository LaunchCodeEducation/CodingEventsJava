package org.launchcode.codingevents.security;

import org.launchcode.codingevents.models.Privilege;
import org.launchcode.codingevents.models.Role;
import org.launchcode.codingevents.models.User;
import org.launchcode.codingevents.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    @Autowired
    private UserService userService;

    public boolean hasPrivilege(String privilege) {
        final User theUser = userService.getCurrentUser();
        if (theUser == null) {
            return false;
        }
        Boolean hasPrivilege = theUser.getRoles()
            .stream()
            .map(Role::getPrivileges)
            .flatMap(coll -> coll.stream())
            .map(Privilege::getName)
            .anyMatch(p -> p.equals(privilege));
        return hasPrivilege;
    }

    public boolean hasRole(String role) {
        final User theUser = userService.getCurrentUser();
        if (theUser == null) {
            return false;
        }
        Boolean hasRole = theUser.getRoles()
            .stream()
            .map(Role::getName)
            .anyMatch(r -> r.equals(role));
        return hasRole;
    }
}
