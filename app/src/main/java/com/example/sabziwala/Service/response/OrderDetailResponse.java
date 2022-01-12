package com.example.sabziwala.Service.response;

public class OrderDetailResponse extends WebResponse{
    OrderDetailResponseData data;

    public OrderDetailResponseData getData() {
        return data;
    }

    public void setData(OrderDetailResponseData data) {
        this.data = data;
    }
}
