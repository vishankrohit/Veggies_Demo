package com.livcorp.veggiesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.livcorp.veggiesdemo.Adapters.AddAddressAdapter;
import com.livcorp.veggiesdemo.Adapters.CheckAdapter;
import com.livcorp.veggiesdemo.Adapters.DetailsAdapter;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.OrderModel;
import com.livcorp.veggiesdemo.Models.UserModel;
import com.livcorp.veggiesdemo.databinding.ActivityDetailsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    RadioButton radioButton;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    FirebaseDatabase database;
    ArrayList<CartModel> cart;
    boolean isAddressAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserModel model = new UserModel();
        OrderModel order = new OrderModel();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        String id = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        database = FirebaseDatabase.getInstance();
        Bundle bundleObject = getIntent().getExtras();
        cart = (ArrayList<CartModel>) bundleObject.getSerializable("CartList");


        DetailsAdapter display = new DetailsAdapter(model, this);
        AddAddressAdapter addAddress = new AddAddressAdapter(model, this, binding.recyclerViewAddress);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String area_City = snapshot.child("area_City").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String pinCode = snapshot.child("pinCode").getValue().toString();
                String state = snapshot.child("state").getValue().toString();

                model.setName(name);
                model.setAddress(address);
                model.setArea_City(area_City);
                model.setPhone(phone);
                model.setPinCode(pinCode);
                model.setState(state);

                if (model.getName().equals("empty")) {
                    binding.recyclerViewAddress.setAdapter(addAddress);
                } else {
                    binding.recyclerViewAddress.setAdapter(display);
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);
                binding.recyclerViewAddress.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CheckAdapter adapter = new CheckAdapter(cart, DetailsActivity.this);
        binding.recyclerViewCheck.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);
        binding.recyclerViewCheck.setLayoutManager(layoutManager);


        int TotalPrice = getIntent().getIntExtra("Total", 0);
        binding.tvTotal.setText("₹ " + Integer.toString(TotalPrice));


        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioID = binding.radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioID);
                order.setCart(cart);
                order.setPaymentMethod(radioButton.getText().toString());
                order.setTotal(Integer.toString(TotalPrice));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        if(name.equals("empty")){
                            isAddressAdded = false;
                            Toast.makeText(DetailsActivity.this, "Please add the Delivery Address.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            isAddressAdded = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(isAddressAdded){
                    reference.child("Order").setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CartModel cartModel = new CartModel();
                                cart.clear();
                                cartModel.saveData(cart,DetailsActivity.this);
                                Map<String,String> map = new HashMap<>();
                                map.put("OrderID",id);
                                firestore.collection("ORDERS").add(map);
                                Toast.makeText(DetailsActivity.this, "Your Order has been placed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailsActivity.this, OrderConfirmationActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}