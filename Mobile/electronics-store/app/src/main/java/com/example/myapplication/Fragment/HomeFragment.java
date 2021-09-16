package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CategoriesRecyclerviewAdapter;
import com.example.myapplication.Adapter.ProductRecyclerviewAdapter;
import com.example.myapplication.Dialog.NewCategoryDialog;
import com.example.myapplication.Dialog.NewProductDialog;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private List<Categories> categoriesList;
    private List<Products> productsList;
    private RecyclerView rcv, rcvProduct;
    private CategoriesRecyclerviewAdapter adapter;
    private AutoCompleteTextView dropdown;
    private AccessTokenManager tokenManager;
    private ArrayAdapter<String> arrayAdapter;
    private String[] dropdownTitle = {"Latest", "A - Z", "Z - A"};
    private ProductRecyclerviewAdapter productRecyclerviewAdapter;
    private RelativeLayout addNewCategory, addNewProduct;
    private IRetrofitService service;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcv = view.findViewById(R.id.rcv_categories);
        rcvProduct = view.findViewById(R.id.rcv_products);
        dropdown = view.findViewById(R.id.sortBy);
        addNewCategory = view.findViewById(R.id.addNewCategoryButton);
        addNewProduct = view.findViewById(R.id.addNewProductButton);
        checkRole();
//
        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryDialog();
            }
        });
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDialog();
            }
        });
// Filter not working
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dropdownTitle);
        dropdown.setText("Latest", false);
        dropdown.setAdapter(arrayAdapter);
//
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tokenManager = AccessTokenManager.getInstance(getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        categoriesList = new ArrayList<>();
        productsList = new ArrayList<>();
        service = RetrofitBuilder.createServices(IRetrofitService.class);
//        Categories
        service.getCategories().enqueue(categoriesCallback);
//        Products
        service.getProducts().enqueue(productsCallback);
    }

    // Call back
    Callback<List<Categories>> categoriesCallback = new Callback<List<Categories>>() {
        @Override
        public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
            if (response.isSuccessful()) {
                categoriesList = response.body();
                adapter = new CategoriesRecyclerviewAdapter(getContext(), categoriesList);
                rcv.setAdapter(adapter);
                rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                rcv.setNestedScrollingEnabled(false);
            }
        }
        @Override
        public void onFailure(Call<List<Categories>> call, Throwable t) {
            Log.d("Error", t.getMessage());
        }
    };
//
    Callback<List<Products>> productsCallback = new Callback<List<Products>>() {
        @Override
        public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
            if (response.isSuccessful()) {
                productsList = response.body();
                productRecyclerviewAdapter = new ProductRecyclerviewAdapter(getContext(), productsList);
                rcvProduct.setAdapter(productRecyclerviewAdapter);
                rcvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rcvProduct.setNestedScrollingEnabled(false);
            }
        }
        @Override
        public void onFailure(Call<List<Products>> call, Throwable t) {
            Log.d("Error", t.getMessage());
        }
    };

    private void checkRole() {
        if (tokenManager.getRole().getRole() == 1) {
            addNewProduct.setVisibility(View.GONE);
            addNewCategory.setVisibility(View.GONE);
        } else {
            addNewProduct.setVisibility(View.VISIBLE);
            addNewCategory.setVisibility(View.VISIBLE);
        }
    }

    private void openCategoryDialog() {
        NewCategoryDialog dialog = new NewCategoryDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "Add Category");
    }

    private void openProductDialog() {
        NewProductDialog dialog = new NewProductDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "Add Product");
    }

}