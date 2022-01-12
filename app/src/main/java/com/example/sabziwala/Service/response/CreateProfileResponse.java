package com.example.sabziwala.Service.response;

public class CreateProfileResponse extends WebResponse {

    private CreateProfileData data;

    public CreateProfileData getData() {
        return data;
    }

    public void setData(CreateProfileData data) {
        this.data = data;
    }
}
