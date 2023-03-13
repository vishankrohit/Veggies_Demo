package com.livcorp.veggiesdemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livcorp.veggiesdemo.DetailsActivity;
import com.livcorp.veggiesdemo.MainActivity;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.viewHolder> {

    ArrayList<CartModel> list;
    Context context;

    public CheckAdapter(ArrayList<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_final_cart_layout, parent, false);
        return new CheckAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ProductModel model = list.get(position).getModel();
        holder.ProductName.setText(model.getName());
        holder.Price.setText(model.getRate());
        int qty = list.get(position).getQuantity();
        String Quantity = Integer.toString(qty);
        holder.tvQuantity.setText("Qty: "+Quantity);
        Picasso.get()
                .load(model.getImg())
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imgProductCart);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView ProductName;
        TextView Price;
        TextView tvQuantity;
        ImageView imgProductCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.tvName);
            Price = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            imgProductCart = itemView.findViewById(R.id.imgProductCart);
        }
    }
}
