package com.example.sabziwala.Service.response;

import java.util.List;

public class GetOrdersResponse extends WebResponse {
    List<GetOrdersResponseData> data;

    public List<GetOrdersResponseData> getData() {
        return data;
    }

    public void setData(List<GetOrdersResponseData> data) {
        this.data = data;
    }
}
