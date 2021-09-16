package com.example.myapplication.Model;

public class Categories {
    private String category_name,image_url;
    private int id;

    public Categories() {
    }

    public Categories(String category_name, String image_url, int id) {
        this.category_name = category_name;
        this.image_url = image_url;
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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
}
