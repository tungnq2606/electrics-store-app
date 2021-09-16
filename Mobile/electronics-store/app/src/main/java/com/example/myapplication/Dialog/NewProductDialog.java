package com.example.myapplication.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductDialog extends DialogFragment {
    private TextInputEditText edName, edPrice;
    private AutoCompleteTextView atCategory;
    private CardView cancel, insert, select_img;
    private Uri imageUri;
    private IRetrofitService service;
    private ArrayAdapter adapter;
    private ImageView img;
    private List<Categories> categoriesList;
    private String url_download = null;
    private Dialog dialog;
    private FirebaseStorage storage;
    private TextView imageError;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.product_form, null);
        edName = view.findViewById(R.id.newProductName);
        edPrice = view.findViewById(R.id.newPrice);
        atCategory = view.findViewById(R.id.selectCategory);
        cancel = view.findViewById(R.id.cancelAddNewProduct);
        insert = view.findViewById(R.id.insertAddNewProduct);
        select_img = view.findViewById(R.id.select_imgProduct);
        imageError = view.findViewById(R.id.newProductImage_Error);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        service.getCategories().enqueue(categoriesCallback);
        img = view.findViewById(R.id.newProductImage);
        imageError.setVisibility(View.GONE);


//
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(1300, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        dialog.setCancelable(false);
//

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    imageError.setVisibility(View.GONE);
                    String categoryName = atCategory.getText().toString();
                    String productName = edName.getText().toString();
                    float price = Float.parseFloat(edPrice.getText().toString());
                    AddProduct(categoryName, productName, price);
                } else {
                    imageError.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        return dialog;
    }

    private void SelectImage() {
        startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageError.setVisibility(View.GONE);
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void AddProduct(String category_name, String product_name, float price) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
        Date now = new Date();
        String filename = dateFormat.format(now);
        storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference(filename);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Url download", uri.toString());
                        url_download = uri.toString();
                        service = RetrofitBuilder.createServices(IRetrofitService.class);
                        service.addProduct(new Products(product_name, url_download, category_name, price, 0)).enqueue(addProductCallback);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Err", e.getMessage());
            }
        });
    }

    Callback<List<Categories>> categoriesCallback = new Callback<List<Categories>>() {
        @Override
        public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
            if (response.isSuccessful()) {
                categoriesList = response.body();
                List<String> select_items = new ArrayList<>();
                for (Categories category : categoriesList) {
                    select_items.add(category.getCategory_name());
                }
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, select_items);
                atCategory.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(Call<List<Categories>> call, Throwable t) {
            Log.d("Error", t.getMessage());
        }
    };

    Callback<String> addProductCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.replace(R.id.navigation_body, new HomeFragment(), "Home").commit();
                Toast.makeText(getContext(), "Insert successful", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    };
}
