package org.launchcode.codingevents.dto;

import jakarta.validation.constraints.Size;

public class EventCategoryDTO {
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    public EventCategoryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
