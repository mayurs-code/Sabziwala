package com.example.sabziwala.Service.response;

import java.io.Serializable;

public class WebErrorResponse implements Serializable {

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
