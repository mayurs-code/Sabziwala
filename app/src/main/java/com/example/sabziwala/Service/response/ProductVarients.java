package com.example.sabziwala.Service.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProductVarients implements Serializable {
    public static final Parcelable.Creator<ProductVarients> CREATOR = new Parcelable.Creator<ProductVarients>() {
        @Override
        public ProductVarients createFromParcel(Parcel source) {
            return new ProductVarients(source);
        }

        @Override
        public ProductVarients[] newArray(int size) {
            return new ProductVarients[size];
        }
    };
    private String size;
    private String unit;
    private String id;
    private String display_price;
    private String price;
    private String cart_quantity;

    public ProductVarients() {
    }

    protected ProductVarients(Parcel in) {
        this.size = in.readString();
        this.unit = in.readString();
        this.price = in.readString();
    }

    public String getDisplay_price() {
        return display_price;
    }

    public void setDisplay_price(String display_price) {
        this.display_price = display_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public void readFromParcel(Parcel source) {
        this.size = source.readString();
        this.unit = source.readString();
        this.price = source.readString();
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }
}
