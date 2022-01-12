package com.example.sabziwala.Service.response;

public class OffersResponseData {
    private String id;
    private String code;
    private String discount_rate;
    private String discount_unit;
    private String valid_from;
    private String valid_to;
    private String uses_count;
    private String min_cart_amount;
    private String status;
    private String created_date;
    private String is_deleted;

    public OffersResponseData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getDiscount_unit() {
        return discount_unit;
    }

    public void setDiscount_unit(String discount_unit) {
        this.discount_unit = discount_unit;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getUses_count() {
        return uses_count;
    }

    public void setUses_count(String uses_count) {
        this.uses_count = uses_count;
    }

    public String getMin_cart_amount() {
        return min_cart_amount;
    }

    public void setMin_cart_amount(String min_cart_amount) {
        this.min_cart_amount = min_cart_amount;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
