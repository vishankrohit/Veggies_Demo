package com.livcorp.veggiesdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.livcorp.veggiesdemo.Adapters.ExpandProductAdapter;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.databinding.ActivityExpandBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExpandActivity extends AppCompatActivity {

    ActivityExpandBinding binding;
    FirebaseFirestore firestore;
    ArrayList<ProductModel> fruits;
    ArrayList<ProductModel> vegetables;
    ArrayList<CartModel> Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Cart = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        boolean isNull = getIntent().getBooleanExtra("isNull", false);
        if (isNull) {
            Bundle bundleObject = getIntent().getExtras();
            Cart = (ArrayList<CartModel>) bundleObject.getSerializable("CartList");
        }
        Cart = loadData();
        int quantity = 0;
        for (int i = 0; i < Cart.size(); i++) {
            quantity = quantity + Cart.get(i).getQuantity();
        }
        binding.tvCart.setText("Cart(" + Integer.toString(quantity) + ")");
        fruits = new ArrayList<>();
        vegetables = new ArrayList<>();

        int Category = getIntent().getIntExtra("Category", 1);
        if (Category == 1) {
            binding.Title.setText("Fruits");

            firestore.collection("CATEGORIES")
                    .document("Fruits")
                    .collection("ExpandPage")
                    .document("fXjW92eIrXp8h9OzNpgC")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String no_of_productsStr = documentSnapshot.get("no_of_products").toString();
                                int no_of_products = Integer.parseInt(no_of_productsStr);
                                for (int i = 1; i < no_of_products + 1; i++) {
                                    fruits.add(new ProductModel(documentSnapshot.get("product_name_" + i).toString()
                                            , documentSnapshot.get("product_rate_" + i).toString()
                                            , Integer.parseInt(documentSnapshot.get("product_price_" + i).toString())
                                            , documentSnapshot.get("product_img_" + i).toString()));
                                }
                                ExpandProductAdapter adapterFruits = new ExpandProductAdapter(fruits, Cart, ExpandActivity.this, binding.tvCart);
                                binding.recyclerViewExpand.setAdapter(adapterFruits);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ExpandActivity.this, 2, GridLayoutManager.VERTICAL, false);
                                binding.recyclerViewExpand.setLayoutManager(gridLayoutManager);
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(ExpandActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            binding.Title.setText("Vegetables");

            firestore.collection("CATEGORIES")
                    .document("Vegetables")
                    .collection("Homepage")
                    .document("xJBPRRuH2A5manyK4gY7")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String no_of_productsStr = documentSnapshot.get("no_of_products").toString();
                                int no_of_products = Integer.parseInt(no_of_productsStr);
                                for (int i = 1; i < no_of_products + 1; i++) {
                                    vegetables.add(new ProductModel(documentSnapshot.get("product_name_" + i).toString()
                                            , documentSnapshot.get("product_rate_" + i).toString()
                                            , Integer.parseInt(documentSnapshot.get("product_price_" + i).toString())
                                            , documentSnapshot.get("product_img_" + i).toString()));
                                }

                                ExpandProductAdapter adapterVegetables = new ExpandProductAdapter(vegetables, Cart, ExpandActivity.this, binding.tvCart);
                                binding.recyclerViewExpand.setAdapter(adapterVegetables);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ExpandActivity.this, 2, GridLayoutManager.VERTICAL, false);
                                binding.recyclerViewExpand.setLayoutManager(gridLayoutManager);

                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(ExpandActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        binding.tvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cart.isEmpty()) {
                    Intent intent = new Intent(ExpandActivity.this, CartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CartList", Cart);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(ExpandActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpandActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private ArrayList<CartModel> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CartModel>>() {
        }.getType();
        Cart = gson.fromJson(json, type);

        if (Cart == null) {
            Cart = new ArrayList<>();
        }
        return Cart;
    }
}