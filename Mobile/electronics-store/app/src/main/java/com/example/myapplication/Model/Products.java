package com.example.myapplication.Model;

public class Products {
    private String product_name, image_url, category_name;
    private float price;
    private int category_id, id;

    public Products() {
    }

    public Products(String product_name, String image_url, float price, int category_id) {
        this.product_name = product_name;
        this.image_url = image_url;
        this.price = price;
        this.category_id = category_id;
    }

    public Products(String product_name, String image_url, String category_name, float price, int id) {
        this.product_name = product_name;
        this.image_url = image_url;
        this.category_name = category_name;
        this.price = price;
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
