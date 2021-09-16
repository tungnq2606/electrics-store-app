package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ProductRecyclerviewAdapter;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.example.myapplication.Screen.SignInScreen;
import com.example.myapplication.Screen.SignUpScreen;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private List<Products> productsList;
    private RecyclerView rcv;
    private AccessTokenManager tokenManager;
    private ProductRecyclerviewAdapter productRecyclerviewAdapter;
    private RelativeLayout notLogin;
    private Button signIn, signUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        notLogin = view.findViewById(R.id.favoriteNotLogin);
        rcv = view.findViewById(R.id.rcv_favorite);
        signIn = view.findViewById(R.id.favoriteSignIn);
        tokenManager = AccessTokenManager.getInstance(getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        signUp = view.findViewById(R.id.favoriteSignUp);
//
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpScreen.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignInScreen.class);
                getFragmentManager().beginTransaction().remove(FavoriteFragment.this);
                intent.putExtra("Started", "Favorite");
                startActivity(intent);
            }
        });
//
        ChangeLayout();
        return view;
    }

    public void ChangeLayout() {

        if (tokenManager.getToken().getAccess_token() != null) {
            rcv.setVisibility(View.VISIBLE);
            notLogin.setVisibility(View.GONE);
            productsList = new ArrayList<>();

            productRecyclerviewAdapter = new ProductRecyclerviewAdapter(getContext(), productsList);
            rcv.setAdapter(productRecyclerviewAdapter);
            rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            rcv.setNestedScrollingEnabled(false);
        } else {
            rcv.setVisibility(View.GONE);
            notLogin.setVisibility(View.VISIBLE);
        }
    }
}