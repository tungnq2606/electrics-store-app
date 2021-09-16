package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CartRecyclerviewAdapter extends RecyclerView.Adapter<CartRecyclerviewAdapter.ViewHolder> {
    private Context context;
    private List<Cart> data;

    public CartRecyclerviewAdapter(Context context, List<Cart> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(data.get(position).getProduct_name());
        holder.price.setText(String.valueOf(data.get(position).getPrice()));
        Glide.with(context).load(data.get(position).getImage_url()).into(holder.img);
        holder.quantity.setText(String.valueOf(data.get(position).getQuantity()));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity += 1;
                holder.quantity.setText(String.valueOf(quantity));
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity -= 1;
                holder.quantity.setText(String.valueOf(quantity));
            }
        });
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded);
//                builder.setTitle("Remove");
//                builder.setMessage("Do you want to remove product ?");
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        data.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//                builder.show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView img, plus, minus, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            name = itemView.findViewById(R.id.cartName);
            price = itemView.findViewById(R.id.cartPrice);
            img = itemView.findViewById(R.id.cartImage);
            quantity = itemView.findViewById(R.id.quantity);
            delete = itemView.findViewById(R.id.deleteCart);
        }
    }
}
