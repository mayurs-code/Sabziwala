package com.example.sabziwala.Service.request;

public class BannersRequest {
    String type;

    public BannersRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
