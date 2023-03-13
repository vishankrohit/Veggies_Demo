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
import com.livcorp.veggiesdemo.ExpandActivity;
import com.livcorp.veggiesdemo.MainActivity;
import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {

    ArrayList<CartModel> list;
    Context context;
    TextView tvTotal;
    int TotalPrice = 0;
    Button btnCheckout;
    ImageView icBack;
    boolean isMainActivity;

    public CartAdapter(ArrayList<CartModel> list, Context context, TextView tvTotal, int TotalPrice, Button btnCheckout, ImageView icBack, boolean isMainActivity) {
        this.list = list;
        this.context = context;
        this.tvTotal = tvTotal;
        this.TotalPrice = TotalPrice;
        this.btnCheckout = btnCheckout;
        this.icBack = icBack;
        this.isMainActivity = isMainActivity;
    }

    public CartAdapter(ArrayList<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_recyclerview_cart, parent, false);
        return new CartAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ProductModel model = list.get(position).getModel();
        holder.ProductName.setText(model.getName());
        holder.Price.setText(model.getRate());
        int qty = list.get(position).getQuantity();
        String Quantity = Integer.toString(qty);
        holder.tvQuantity.setText(Quantity);Picasso.get()
                .load(model.getImg())
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imgProductCart);

        tvTotal.setText("₹ " + Integer.toString(TotalPrice));
        CartModel cartModel = new CartModel();
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = list.get(position).getQuantity() + 1;
                list.get(position).setQuantity(quantity);
                holder.tvQuantity.setText(Integer.toString(quantity));
                TotalPrice = TotalPrice + list.get(position).getModel().getPrice();
                tvTotal.setText("₹ " + Integer.toString(TotalPrice));
                cartModel.saveData(list, context);
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = list.get(position).getQuantity() - 1;
                list.get(position).setQuantity(quantity);
                holder.tvQuantity.setText(Integer.toString(quantity));
                TotalPrice = TotalPrice - list.get(position).getModel().getPrice();
                tvTotal.setText("₹ " + Integer.toString(TotalPrice));
                if (list.get(position).getQuantity() <= 0) {
                    list.remove(position);
                }
                cartModel.saveData(list, context);
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("Total", TotalPrice);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CartList", list);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMainActivity){
                    Intent intent = new Intent(context, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CartList", list);
                    intent.putExtras(bundle);
                    intent.putExtra("isNull", false);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, ExpandActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CartList", list);
                    intent.putExtras(bundle);
                    intent.putExtra("isNull", false);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView ProductName;
        TextView Price;
        TextView tvQuantity;
        Button btnAdd;
        Button btnRemove;
        ImageView imgProductCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.tvName);
            Price = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            imgProductCart = itemView.findViewById(R.id.imgProductCart);
        }
    }
}
