package com.example.sabziwala.Service.response;

public class GetProfileResponse extends WebResponse
{
    GetProfileResponseData data;

    public GetProfileResponseData getData() {
        return data;
    }

    public void setData(GetProfileResponseData data) {
        this.data = data;
    }
}
