package com.example.sabziwala.Service.response;

import java.util.List;

public class ProductListResponse extends WebResponse{
    public List<ProductListResponseData> getData() {
        return data;
    }

    public void setData(List<ProductListResponseData> data) {
        this.data = data;
    }

    private List<ProductListResponseData> data;
}
