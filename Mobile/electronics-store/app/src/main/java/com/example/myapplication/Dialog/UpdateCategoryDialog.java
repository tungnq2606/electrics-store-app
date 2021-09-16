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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.Categories;
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
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCategoryDialog extends DialogFragment {
    private CardView selectImage;
    private TextView title, imageError,buttonTitle;
    private TextInputEditText categoryName;
    private ImageView img;
    private CardView cancel, insert;
    private IRetrofitService service;
    private Uri imageUri = null;
    private String url_download = null;
    private FirebaseStorage storage;
    private Dialog dialog;
    private String category_name, image_url;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id", 0);
            category_name = getArguments().getString("category_name", "");
            image_url = getArguments().getString("image_url", "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_form, null);
        selectImage = view.findViewById(R.id.select_img);
        categoryName = view.findViewById(R.id.newCategoryName);
        cancel = view.findViewById(R.id.cancelAddNewCategory);
        insert = view.findViewById(R.id.insertAddNewCategory);
        title = view.findViewById(R.id.category_form_title);
        img = view.findViewById(R.id.newCategoryImage);
        imageError = view.findViewById(R.id.newCategoryImage_Eror);
        buttonTitle = view.findViewById(R.id.categoryBtnTitle);
        imageError.setVisibility(View.GONE);
//
        buttonTitle.setText("Update");
        title.setText("Update Category");
        categoryName.setText(category_name);
        Glide.with(getContext()).load(image_url).into(img);
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


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryName.getText().toString();
                Log.d("Name", name);
                UpdateCategory(name);
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
//
        return dialog;
    }

    private void SelectImage() {
        startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void UpdateCategory(String categoryName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
        Date now = new Date();
        String filename = dateFormat.format(now);
        storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference(filename);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        if (imageUri != null) {
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Success", "Success");
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url_download = uri.toString();
                            service.updateCategory(new Categories(categoryName, url_download, id)).enqueue(updateCategoryCallback);
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
            return;
        }
        service.updateCategory(new Categories(categoryName, image_url, id)).enqueue(updateCategoryCallback);
    }


    Callback<String> updateCategoryCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.replace(R.id.navigation_body, new HomeFragment(), "Home").commit();
                Toast.makeText(getContext(), "Update successful", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Update failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.e("err", t.getMessage());
        }
    };
}
