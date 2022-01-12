package com.example.sabziwala.Service.response;

import java.util.List;

public class NotificationResponse extends WebResponse{
    List<NotificationResponseData> data;

    public List<NotificationResponseData> getData() {
        return data;
    }

    public void setData(List<NotificationResponseData> data) {
        this.data = data;
    }
}