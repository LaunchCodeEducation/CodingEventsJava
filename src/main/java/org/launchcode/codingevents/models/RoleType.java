package org.launchcode.codingevents.models;

public enum RoleType {
    ROLE_USER("User"),
    ROLE_ORGANIZER("Organizer"),
    ROLE_ADMIN("Admin");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
