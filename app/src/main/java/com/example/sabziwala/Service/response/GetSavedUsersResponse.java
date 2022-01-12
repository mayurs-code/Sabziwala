package com.example.sabziwala.Service.response;

import java.util.List;

public class GetSavedUsersResponse extends WebResponse {
    List<GetSavedUsersResponseData> data;

    public List<GetSavedUsersResponseData> getData() {
        return data;
    }

    public void setData(List<GetSavedUsersResponseData> data) {
        this.data = data;
    }
}
