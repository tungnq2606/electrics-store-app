package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartRecyclerviewAdapter;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.example.myapplication.Screen.CheckOut;
import com.example.myapplication.Screen.SignInScreen;
import com.example.myapplication.Screen.SignUpScreen;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private List<Cart> list;
    private RecyclerView rcv;
    private CartRecyclerviewAdapter adapter;
    private IRetrofitService service;
    private AppCompatButton goToCheckout;
    private AccessTokenManager tokenManager;
    private RelativeLayout notLogin;
    private NestedScrollView logged;
    private Button signIn, signUp;
    private TextView totalPrice;
    private float total = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        // Inflate the layout for this fragment
        rcv = view.findViewById(R.id.rcv_cart);
        tokenManager = AccessTokenManager.getInstance(getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        goToCheckout = view.findViewById(R.id.goToCheckout);
        notLogin = view.findViewById(R.id.cartNotLogin);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        logged = view.findViewById(R.id.cartLogged);
        totalPrice = view.findViewById(R.id.cartTotal);
        signIn = view.findViewById(R.id.cartSignIn);
        signUp = view.findViewById(R.id.cartSignUp);
        ChangeLayout();
        goToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CheckOut.class);
                startActivity(intent);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignInScreen.class);
                getFragmentManager().beginTransaction().remove(CartFragment.this);
                intent.putExtra("Started", "Cart");
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpScreen.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void ChangeLayout() {

        if (tokenManager.getToken().getAccess_token() != null) {
            if (tokenManager.getRole().getRole() == 1) {
                logged.setVisibility(View.VISIBLE);
                notLogin.setVisibility(View.GONE);
                service.getCart().enqueue(getCartCallback);
            }else{
                logged.setVisibility(View.GONE);
                notLogin.setVisibility(View.GONE);
            }
        } else {
            notLogin.setVisibility(View.VISIBLE);
            logged.setVisibility(View.GONE);
        }
    }

    Callback<List<Cart>> getCartCallback = new Callback<List<Cart>>() {
        @Override
        public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
            list = response.body();
            adapter = new CartRecyclerviewAdapter(getContext(), list);
            rcv.setAdapter(adapter);
            rcv.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv.setNestedScrollingEnabled(false);
            if (list.size() > 0) {
                for (Cart items : list) {
                    total += items.getPrice();
                }
                totalPrice.setText(String.valueOf(total));
            }
        }

        @Override
        public void onFailure(Call<List<Cart>> call, Throwable t) {

        }
    };
}