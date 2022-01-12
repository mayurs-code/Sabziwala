package com.example.sabziwala.Service.response;

import java.io.Serializable;
import java.util.List;

public class CartResponseData implements Serializable {
    List<CartProductListResponseData> products;
    String sub_total;
    String delivery_charge;
    String total;

    public List<CartProductListResponseData> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductListResponseData> products) {
        this.products = products;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
