package com.livcorp.veggiesdemo.Models;

import java.util.ArrayList;

public class OrderModel {
    private ArrayList<CartModel> cart;
    private String paymentMethod;
    private String Total;

    public OrderModel(ArrayList<CartModel> cart, String paymentMethod, String Total) {
        this.cart = cart;
        this.paymentMethod = paymentMethod;
        this.Total = Total;
    }

    public OrderModel() {}

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public ArrayList<CartModel> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartModel> cart) {
        this.cart = cart;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
