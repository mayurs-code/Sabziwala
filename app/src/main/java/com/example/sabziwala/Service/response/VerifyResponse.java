
package com.example.sabziwala.Service.response;

public class VerifyResponse extends WebResponse {

    private String sessionkey;
    private Integer is_new_user;
    private VerifyOtpData data;

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public Integer getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(Integer is_new_user) {
        this.is_new_user = is_new_user;
    }

    public VerifyOtpData getData() {
        return data;
    }

    public void setData(VerifyOtpData data) {
        this.data = data;
    }
}
