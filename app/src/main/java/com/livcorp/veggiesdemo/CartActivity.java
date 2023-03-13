package com.livcorp.veggiesdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livcorp.veggiesdemo.Adapters.CartAdapter;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    ArrayList<CartModel> cart;
    int TotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Bundle bundleObject = getIntent().getExtras();
        cart = (ArrayList<CartModel>) bundleObject.getSerializable("CartList");
        boolean isMainActivity = getIntent().getBooleanExtra("isMainActivity",false);

        for (int i = 0; i < cart.size(); i++) {
            TotalPrice = TotalPrice + (cart.get(i).getModel().getPrice() * cart.get(i).getQuantity());
        }


        CartAdapter adapter = new CartAdapter(cart, this, binding.tvTotal, TotalPrice, binding.btnNext, binding.icBack, isMainActivity);
        binding.recyclerviewCart.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerviewCart.setLayoutManager(layoutManager);


    }
}