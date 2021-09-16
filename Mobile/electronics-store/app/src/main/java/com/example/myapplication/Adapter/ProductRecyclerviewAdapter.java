package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Dialog.UpdateProductDialog;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.example.myapplication.Screen.ProductDetail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRecyclerviewAdapter extends RecyclerView.Adapter<ProductRecyclerviewAdapter.ViewHolder> {
    private Context context;
    private List<Products> data;
    private AccessTokenManager tokenManager;
    private IRetrofitService service;

    public ProductRecyclerviewAdapter(Context context, List<Products> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        tokenManager = AccessTokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getImage_url()).into(holder.img);
        holder.name.setText(data.get(position).getProduct_name());
        holder.price.setText(String.valueOf(data.get(position).getPrice()));
        holder.rate.setText(String.valueOf(4.9));
        if (tokenManager.getRole().getRole() == 1) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetail.class);
                    ((Activity) context).startActivity(intent);
                }
            });
        } else {
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded);
                    builder.setMessage("Do you want to delete ?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.deleteProduct(new Products(null, null, null,
                                    0, data.get(position).getId())).enqueue(deleteProductCallback);
                        }
                    });
                    builder.show();
                    return true;
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateProductDialog dialog = new UpdateProductDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", data.get(position).getId());
                    bundle.putString("category_name", data.get(position).getCategory_name());
                    bundle.putString("image_url", data.get(position).getImage_url());
                    bundle.putString("product_name", data.get(position).getProduct_name());
                    bundle.putFloat("product_price", data.get(position).getPrice());
                    dialog.setArguments(bundle);
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Add Category");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, rate;
        ImageView img;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            rate = itemView.findViewById(R.id.productRating);
            img = itemView.findViewById(R.id.productImage);
            cardView = itemView.findViewById(R.id.wrapProductItem);
        }
    }

    Callback<String> deleteProductCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.replace(R.id.navigation_body, new HomeFragment(), "Home").commit();
                Toast.makeText(context, "Delete successful", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    };
}
