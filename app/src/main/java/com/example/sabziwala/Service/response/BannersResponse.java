package com.example.sabziwala.Service.response;

import java.util.List;

public class BannersResponse extends WebResponse {
    public List<BannersResponseData> getData() {
        return data;
    }

    public void setData(List<BannersResponseData> data) {
        this.data = data;
    }

    private List<BannersResponseData> data;

    public BannersResponse() {
    }
}
