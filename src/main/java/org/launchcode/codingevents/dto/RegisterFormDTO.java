package org.launchcode.codingevents.dto;

public class RegisterFormDTO extends LoginFormDTO {

    private String verifyPassword;

    private Boolean eventOrganizer;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public Boolean getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(Boolean eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
}
