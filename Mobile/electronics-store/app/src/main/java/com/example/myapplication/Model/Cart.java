package com.example.myapplication.Model;

public class Cart {
    private String product_name, image_url;
    private int id, quantity;
    private float price;

    public Cart() {
    }

    public Cart(String product_name, String image_url, int id, int quantity, float price) {
        this.product_name = product_name;
        this.image_url = image_url;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
