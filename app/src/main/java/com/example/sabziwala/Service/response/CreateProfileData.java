package com.example.sabziwala.Service.response;

public class CreateProfileData {
    /* "data": {
        "user_id": "9",
        "phone_number": "1478523690",
        "email": "test@gmail.com",
        "full_name": "test user",
        "profile_image": "",
        "referral_code": "ipr6na",
        "latitude": null,
        "longitude": null,
        "address": [],
        "rating": "2",
        "role": "App User"
    }*/
    private String user_id;
    private String phone_number;
    private String email;
    private String full_name;
    private String profile_image;
    private String role;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
