package com.livcorp.veggiesdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.livcorp.veggiesdemo.Adapters.ProductAdapter;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.databinding.ActivityMainBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore firestore;
    ArrayList<ProductModel> fruits;
    ArrayList<ProductModel> vegetables;
    ArrayList<CartModel> Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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


        firestore.collection("CATEGORIES")
                .document("Fruits")
                .collection("Homepage")
                .document("gu9NsxCaPtwdEbkxNyC2")
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
                            ProductAdapter adapterFruits = new ProductAdapter(fruits, Cart, MainActivity.this, binding.tvCart);
                            binding.recyclerviewFruits.setAdapter(adapterFruits);
                            LinearLayoutManager layoutManagerFruits = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.recyclerviewFruits.setLayoutManager(layoutManagerFruits);
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

                            ProductAdapter adapterVegetables = new ProductAdapter(vegetables, Cart, MainActivity.this, binding.tvCart);
                            binding.recyclerviewVegetables.setAdapter(adapterVegetables);
                            LinearLayoutManager layoutManagerVegetables = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.recyclerviewVegetables.setLayoutManager(layoutManagerVegetables);

                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        binding.tvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cart.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CartList", Cart);
                    intent.putExtras(bundle);
                    intent.putExtra("isMainActivity",true);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.textSeeAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpandActivity.class);
                intent.putExtra("Category", 1);
                startActivity(intent);
            }
        });

        binding.textSeeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpandActivity.class);
                intent.putExtra("Category", 2);
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