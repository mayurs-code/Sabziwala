package com.example.sabziwala.Service.response;

import java.util.List;

public class CategoryResponse extends WebResponse {

    public List<CategoryResponseData> getData() {
        return data;
    }

    public void setData(List<CategoryResponseData> data) {
        this.data = data;
    }

    private List<CategoryResponseData> data;



}
