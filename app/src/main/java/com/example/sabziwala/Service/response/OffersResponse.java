package com.example.sabziwala.Service.response;

import java.util.List;

public class OffersResponse extends WebResponse {
    public List<OffersResponseData> getData() {
        return data;
    }

    public void setData(List<OffersResponseData> data) {
        this.data = data;
    }

    private List<OffersResponseData> data;
}
