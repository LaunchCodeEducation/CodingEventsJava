package org.launchcode.codingevents.models;

import jakarta.persistence.Entity;

@Entity
public class Privilege extends AbstractEntity {

    private String name;

    public Privilege(String name) {
        this.name = name;
    }

    public Privilege() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
