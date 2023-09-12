package org.launchcode.codingevents.security;

import org.launchcode.codingevents.controllers.AuthenticationController;
import org.launchcode.codingevents.models.Privilege;
import org.launchcode.codingevents.models.Role;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
public class SecurityService {
    @Autowired
    private AuthenticationController authController;

    public boolean hasPrivilege(String privilege) {
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpSession session = attr.getRequest().getSession(false);
        final User theUser = authController.getUserFromSession(session);
        if (session != null && theUser != null) {
            Boolean hasPrivilege = theUser.getRoles()
                .stream()
                .map(Role::getPrivileges)
                .flatMap(coll -> coll.stream())
                .map(Privilege::getName)
                .anyMatch(p -> p.equals(privilege));
            return hasPrivilege;
        }
        return false;
    }

    public boolean hasRole(String role) {
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpSession session = attr.getRequest().getSession(false);
        final User theUser = authController.getUserFromSession(session);
        if (session != null && theUser != null) {
            Boolean hasRole = theUser.getRoles()
                .stream()
                .map(Role::getName)
                .anyMatch(r -> r.equals(role));
            return hasRole;
        }
        return false;
    }
}
