package com.example.sabziwala.Service.response;

import java.util.List;

public class GetInventoryResponse extends  WebResponse{
    List<GetInventoryResponseData> data;

    public List<GetInventoryResponseData> getData() {
        return data;
    }

    public void setData(List<GetInventoryResponseData> data) {
        this.data = data;
    }
}
