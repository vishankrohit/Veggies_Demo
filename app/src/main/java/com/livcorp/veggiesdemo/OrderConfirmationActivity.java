package com.livcorp.veggiesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.livcorp.veggiesdemo.Adapters.CheckAdapter;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.OrderModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.Models.UserModel;
import com.livcorp.veggiesdemo.databinding.ActivityOrderConfirmationBinding;

import java.util.ArrayList;

public class OrderConfirmationActivity extends AppCompatActivity {

    ActivityOrderConfirmationBinding binding;
    DatabaseReference reference;
    ArrayList<CartModel> cart;
    String id;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OrderModel order = new OrderModel();
        UserModel user = new UserModel();
        cart = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        id = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String area_City = snapshot.child("area_City").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String pinCode = snapshot.child("pinCode").getValue().toString();
                String state = snapshot.child("state").getValue().toString();

                user.setName(name);
                user.setAddress(address);
                user.setArea_City(area_City);
                user.setPhone(phone);
                user.setPinCode(pinCode);
                user.setState(state);
                order.setPaymentMethod(snapshot.child("Order").child("paymentMethod").getValue().toString());
                order.setTotal(snapshot.child("Order").child("total").getValue().toString());

                GenericTypeIndicator<ArrayList<CartModel>> t = new GenericTypeIndicator<ArrayList<CartModel>>() {
                };
                cart = snapshot.child("Order").child("cart").getValue(t);

                CheckAdapter adapter = new CheckAdapter(cart, OrderConfirmationActivity.this);
                binding.recyclerViewCheck.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(OrderConfirmationActivity.this);
                binding.recyclerViewCheck.setLayoutManager(layoutManager);

                binding.tvPaymentMethod.setText(order.getPaymentMethod());
                binding.tvUserName.setText(user.getName());
                binding.tvAddress.setText(user.getAddress()+", "+user.getArea_City()+", "+user.getState()+", "+user.getPinCode());
                binding.tvPhone.setText(user.getPhone());
                binding.tvTotal.setText("₹ " + order.getTotal());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}