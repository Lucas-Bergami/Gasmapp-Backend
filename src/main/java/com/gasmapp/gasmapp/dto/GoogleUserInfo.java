package com.gasmapp.gasmapp.dto;

public class GoogleUserInfo {

    private String email;
    private String name;
    private String sub; // ID único do Google

    public GoogleUserInfo(String email, String name, String sub) {
        this.email = email;
        this.name = name;
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSub() {
        return sub;
    }
}
