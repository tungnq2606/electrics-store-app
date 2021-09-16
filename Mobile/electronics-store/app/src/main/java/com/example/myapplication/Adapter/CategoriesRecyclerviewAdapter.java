package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Dialog.UpdateCategoryDialog;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesRecyclerviewAdapter extends RecyclerView.Adapter<CategoriesRecyclerviewAdapter.ViewHolder> {
    private Context context;
    private IRetrofitService service;
    private List<Categories> data;
    private AccessTokenManager tokenManager;

    public CategoriesRecyclerviewAdapter(Context context, List<Categories> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        tokenManager = AccessTokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_custom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tokenManager.getRole().getRole() == 1) {
            holder.remove.setVisibility(View.GONE);
        } else {
            holder.remove.setVisibility(View.VISIBLE);
            holder.bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateCategoryDialog dialog = new UpdateCategoryDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", data.get(position).getId());
                    bundle.putString("category_name", data.get(position).getCategory_name());
                    bundle.putString("image_url", data.get(position).getImage_url());
                    dialog.setArguments(bundle);
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Add Category");
                }
            });
        }
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.d("ID", String.valueOf(data.get(position).getId()));
                        service.deleteCategory(new Categories(null, null, data.get(position).getId())).enqueue(deleteCategoryCallback);
                    }
                });
                builder.show();
            }
        });
        holder.title.setText(data.get(position).getCategory_name());
        Glide.with(context).load(data.get(position).getImage_url()).into(holder.bg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView bg, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categoryTitle);
            bg = itemView.findViewById(R.id.categoryBG);
            remove = itemView.findViewById(R.id.remove_categoryBtn);
        }
    }

    Callback<String> deleteCategoryCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.replace(R.id.navigation_body, new HomeFragment(), "Home").commit();
                Toast.makeText(context, "Delete successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.e("Err", t.getMessage());
        }
    };
}
