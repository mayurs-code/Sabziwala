package com.example.sabziwala.Service.response;

public class UserLoginResponse extends WebResponse {
    String token;
    UserLoginResponseData data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserLoginResponseData getData() {
        return data;
    }

    public void setData(UserLoginResponseData data) {
        this.data = data;
    }
}
