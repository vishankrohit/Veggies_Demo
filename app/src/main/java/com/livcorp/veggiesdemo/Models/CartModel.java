package com.livcorp.veggiesdemo.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CartModel implements Serializable {
    private ProductModel product;
    private int quantity = 0;

    public CartModel(ProductModel model, int quantity) {
        this.product = model;
        this.quantity = quantity;
    }

    public CartModel() {
    }

    public ProductModel getModel() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setModel(ProductModel model) {
        this.product = model;
    }

    public boolean Equals(CartModel cart, ProductModel model) {
        String Name = cart.product.getName();
        String Rate = cart.product.getRate();
        int price = cart.product.getPrice();

        if (Name.equals(model.getName()) && Rate.equals(model.getRate()) && price == model.getPrice()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Contains(ArrayList<CartModel> cart, ProductModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).Equals(cart.get(i), model)) {
                updateQuantity(cart, i);
                return true;
            }
        }
        return false;
    }

    public void updateQuantity(ArrayList<CartModel> cart, int position) {
        cart.get(position).setQuantity(cart.get(position).getQuantity() + 1);
    }

    public void saveData(ArrayList<CartModel> Cart, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Cart);
        editor.putString("task list", json);
        editor.apply();
    }

    public void saveTotalAndQty(int Total, int quantity, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Total", Total);
        editor.putInt("Quantity", quantity);
        editor.apply();
    }

}
