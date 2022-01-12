package com.example.sabziwala.Service.response;

public class CartResponse extends WebResponse{

    CartResponseData data;

    public CartResponseData getData() {
        return data;
    }

    public void setData(CartResponseData data) {
        this.data = data;
    }
}
