package com.example.sabziwala.Service.response;

import java.io.Serializable;
import java.util.List;

public class ProductListResponseData implements Serializable {
    private List<ProductVarients> variants;

    public void setVariants(List<ProductVarients> variants) {
        this.variants = variants;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    private List<ImageResponse> images;
    private String id;
    private String user_id;
    private String name;
    private String category_id;
    private String discount;
    private String product_detail;
    private String nutritions;
    private String benifits;
    private String storage_and_uses;
    private String image;
    private String status;
    private String created_date;
    private String is_deleted;
    private String added_in_cart;

    public ProductListResponseData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public String getNutritions() {
        return nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

    public String getBenifits() {
        return benifits;
    }

    public void setBenifits(String benifits) {
        this.benifits = benifits;
    }

    public String getStorage_and_uses() {
        return storage_and_uses;
    }

    public void setStorage_and_uses(String storage_and_uses) {
        this.storage_and_uses = storage_and_uses;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
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

    public String getAdded_in_cart() {
        return added_in_cart;
    }

    public void setAdded_in_cart(String added_in_cart) {
        this.added_in_cart = added_in_cart;
    }

    public List<ProductVarients> getVariants() {
        return variants;
    }

    public void setData(List<ProductVarients> data) {
        this.variants = data;
    }
}
