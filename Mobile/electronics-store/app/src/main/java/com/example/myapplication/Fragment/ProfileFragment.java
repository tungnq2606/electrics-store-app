package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.User;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.example.myapplication.Screen.ChangePasswordScreen;
import com.example.myapplication.Screen.SignInScreen;
import com.example.myapplication.Screen.SignUpScreen;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView name, email, logout, changePassword;
    private Button SignIn, SignUp;
    private LinearLayout notLogin, wrLogOut;
    private RelativeLayout logged;
    private AccessTokenManager tokenManager;
    private IRetrofitService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
        SignIn = view.findViewById(R.id.profileSignIn);
        SignUp = view.findViewById(R.id.profileSignUp);
        logout = view.findViewById(R.id.logOut);
        notLogin = view.findViewById(R.id.notLogin);
        logged = view.findViewById(R.id.logged);
        tokenManager = AccessTokenManager.getInstance(getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        wrLogOut = view.findViewById(R.id.wrLogOut);
        changePassword = view.findViewById(R.id.changePassword);
        ChangeLayout();
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpScreen.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordScreen.class);
                intent.putExtra("email", email.getText().toString());
                startActivity(intent);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignInScreen.class);
                getFragmentManager().beginTransaction().remove(ProfileFragment.this);
                intent.putExtra("Started", "Profile");
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialog_rounded);
                builder.setTitle("Log Out");
                builder.setMessage("Do you want to log out ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tokenManager.deleteToken();
                        tokenManager.deleteRole();
                        ChangeLayout();
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    Callback<User> getProfileCallback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                User user = response.body();
                name.setText(user.getFullname());
                email.setText(user.getEmail());
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {

        }
    };

    public void ChangeLayout() {

        if (tokenManager.getToken().getAccess_token() != null) {
            logged.setVisibility(View.VISIBLE);
            wrLogOut.setVisibility(View.VISIBLE);
            notLogin.setVisibility(View.GONE);
            service = RetrofitBuilder.createServices(IRetrofitService.class);
            service.getProfile().enqueue(getProfileCallback);
        } else {
            logged.setVisibility(View.GONE);
            wrLogOut.setVisibility(View.GONE);
            notLogin.setVisibility(View.VISIBLE);
        }
    }

}