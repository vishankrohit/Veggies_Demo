package com.livcorp.veggiesdemo.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livcorp.veggiesdemo.Models.UserModel;
import com.livcorp.veggiesdemo.R;
import com.google.android.material.textfield.TextInputEditText;



public class AddAddressAdapter extends RecyclerView.Adapter<AddAddressAdapter.viewHolder>{

    UserModel model;
    Context context;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public AddAddressAdapter(UserModel model, Context context, RecyclerView recyclerView){
        this.model = model;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public AddAddressAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_add_new_address_details,parent,false);
        return new AddAddressAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAddressAdapter.viewHolder holder, int position) {
        holder.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCard();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        Button btnAddAddress;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            btnAddAddress = itemView.findViewById(R.id.btnAddDetails);
        }
    }
    public void showDialogCard(){
        auth = FirebaseAuth.getInstance();
        String id = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputEditText etName = dialog.findViewById(R.id.etName);
        TextInputEditText etAddress = dialog.findViewById(R.id.etAddress);
        TextInputEditText etAreaAndCity = dialog.findViewById(R.id.etAreaAndCity);
        TextInputEditText etState = dialog.findViewById(R.id.etState);
        TextInputEditText etPinCode = dialog.findViewById(R.id.etPinCode);
        TextInputEditText etPhone = dialog.findViewById(R.id.etPhone);
        Button btnSaveDetails = dialog.findViewById(R.id.btnSaveDetails);
        dialog.show();
        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = etName.getText().toString();
                String Address = etAddress.getText().toString();
                String AreaAndCity = etAreaAndCity.getText().toString();
                String State = etState.getText().toString();
                String PinCode = etPinCode.getText().toString();
                String Phone = etPhone.getText().toString();

                model.setName(Name);
                model.setAddress(Address);
                model.setArea_City(AreaAndCity);
                model.setState(State);
                model.setPinCode(PinCode);
                model.setPhone(Phone);

                database.getReference()
                        .child("Users")
                        .child(id)
                        .setValue(model);

                dialog.dismiss();

                DetailsAdapter adapter = new DetailsAdapter(model, context);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}
