package com.livcorp.veggiesdemo.Models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String Name;
    private String Rate;
    private int price;
    private String img;

    public ProductModel(String name, String rate, int price, String img) {
        Name = name;
        Rate = rate;
        this.price = price;
        this.img = img;
    }

    public ProductModel() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}
