package com.example.sabziwala.Service.request;

public class UpdateProfileRequest {
/*{"full_name":"test user","email":"test@gmail.com","phone_number":"1478523690","fcm_token":"12345"}*/
    private String full_name;
    private String email;
    private String phone_number;
    private String fcm_token;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}