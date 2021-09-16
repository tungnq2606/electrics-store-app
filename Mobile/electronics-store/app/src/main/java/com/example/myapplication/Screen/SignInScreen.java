package com.example.myapplication.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.AccessToken;
import com.example.myapplication.Model.AccessTokenManager;
import com.example.myapplication.Model.Account;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInScreen extends AppCompatActivity {
    private TextView goToSignUp, forgotPassword;
    private RelativeLayout signInButton;
    private TextInputEditText password, email;
    private IRetrofitService service;
    private AccessTokenManager tokenManager;
    private TextInputLayout emailError, passwordError;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        goToSignUp = findViewById(R.id.goToSignUp);
        signInButton = findViewById(R.id.signInButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        emailError = findViewById(R.id.usernameTF);
        passwordError = findViewById(R.id.passwordTF);
        progressBar = findViewById(R.id.signInProgress);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        progressBar.setVisibility(View.INVISIBLE);
        tokenManager = AccessTokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    emailError.setErrorEnabled(false);
                    emailError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    passwordError.setErrorEnabled(false);
                    passwordError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this, EnterEmail.class);
                startActivity(intent);
            }
        });

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this, SignUpScreen.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us = email.getText().toString().trim();
                String pw = password.getText().toString().trim();
                if (us.length() > 0 && pw.length() > 0) {
                    service.login(new Account(us, pw)).enqueue(loginCallback);
                } else {
                    if (us.length() < 1) {
                        emailError.setErrorEnabled(true);
                        emailError.setError("Email is require");
                    }
                    if (pw.length() < 1) {
                        passwordError.setErrorEnabled(true);
                        passwordError.setError("Password is require");
                    }
                }
            }
        });
    }

    Callback<AccessToken> loginCallback = new Callback<AccessToken>() {
        @Override
        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
            if (response.isSuccessful()) {
                AccessToken token = response.body();
                tokenManager.saveToken(token);
                tokenManager.saveRole(token);
                Toast.makeText(SignInScreen.this, "Sign in success", Toast.LENGTH_SHORT).show();
                switch (getIntent().getStringExtra("Started")) {
                    case "SendEmail":
                        Intent intent = new Intent(SignInScreen.this, BottomNav.class);
                        startActivity(intent);
                        return;
                    default:
                        BottomNav.tagName = getIntent().getStringExtra("Started");
                        break;
                }
                finish();
            }
            if (response.code() == 401) {
                Toast.makeText(SignInScreen.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<AccessToken> call, Throwable t) {

        }

    };

}