package com.example.sabziwala.Service.request;

public class VerifyOtpRequest {

    private String country_code;
    private String phone_number;
  //  private String otp;
    private String device_type;

    public String getCountryCode() {
        return country_code;
    }

    public void setCountryCode(String countryCode) {
        this.country_code = countryCode;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

  /*  public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
*/
    public String getDeviceType() {
        return device_type;
    }

    public void setDeviceType(String deviceType) {
        this.device_type = deviceType;
    }

}