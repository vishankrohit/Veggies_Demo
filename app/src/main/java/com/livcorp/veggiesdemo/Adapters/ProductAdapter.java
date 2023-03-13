package com.livcorp.veggiesdemo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livcorp.veggiesdemo.Models.CartModel;
import com.livcorp.veggiesdemo.Models.ProductModel;
import com.livcorp.veggiesdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {

    ArrayList<ProductModel> list;
    ArrayList<CartModel> cart;
    TextView tvCartSize;
    Context context;

    public ProductAdapter(ArrayList<ProductModel> list, ArrayList<CartModel> cart, Context context, TextView tvCartSize) {
        this.list = list;
        this.context = context;
        this.cart = cart;
        this.tvCartSize = tvCartSize;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_recylclerview_cards, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.ProductName.setText(model.getName());
        holder.Price.setText(model.getRate());
        Picasso.get()
                .load(model.getImg())
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imgProduct);


        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuantity(cart, model);
                int quantity = 0;
                for (int i = 0; i < cart.size(); i++) {
                    quantity = quantity + cart.get(i).getQuantity();
                }
                CartModel cartModel = new CartModel();
                cartModel.saveData(cart, context);
                tvCartSize.setText("Cart(" + Integer.toString(quantity) + ")");
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
        Button btnAddToCart;
        ImageView imgProduct;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            ProductName = itemView.findViewById(R.id.tvName);
            Price = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    public void checkQuantity(ArrayList<CartModel> cart, ProductModel model) {
        CartModel cartModel = new CartModel();
        if (cartModel.Contains(cart, model)) {
        } else {
            cart.add(new CartModel(model, 1));
        }
    }
}
