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

import com.example.myapplication.Model.User;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpScreen extends AppCompatActivity {
    private TextView goToSignIn;
    private TextInputEditText ed1, ed2, ed3;
    private RelativeLayout signUpButton;
    private TextInputLayout fullNameError, emailError, passwordError;
    private Boolean fullNameIsValid = false;
    private Boolean emailIsValid = false;
    private Boolean passwordIsValid = false;
    private IRetrofitService service;
    private String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]{0,10})*@[A-Za-z0-9]+(\\.[A-Za-z0-9]{0,10})*(\\.[A-Za-z]{0,5})$";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        goToSignIn = findViewById(R.id.goToSignIn);
        ed1 = findViewById(R.id.newEmail);
        ed2 = findViewById(R.id.newFullName);
        ed3 = findViewById(R.id.newPassword);
        fullNameError = findViewById(R.id.signUpFullNameTF);
        emailError = findViewById(R.id.signUpUsernameTF);
        passwordError = findViewById(R.id.signUpPasswordTF);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.signUpProgress);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        progressBar.setVisibility(View.INVISIBLE);
//          Validation form
        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.toString().matches(regex))) {
                    emailError.setErrorEnabled(true);
                    emailError.setError("Email is valid");
                    emailIsValid = false;
                } else {
                    emailError.setErrorEnabled(false);
                    emailError.setError(null);
                    emailIsValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    fullNameError.setErrorEnabled(true);
                    fullNameError.setError("Full name is require");
                    fullNameIsValid = false;
                } else {
                    fullNameError.setErrorEnabled(false);
                    fullNameError.setError(null);
                    fullNameIsValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6) {
                    passwordError.setErrorEnabled(true);
                    passwordError.setError("Password must be more than 6 characters");
                    passwordIsValid = false;
                } else {
                    passwordError.setErrorEnabled(false);
                    passwordError.setError(null);
                    passwordIsValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed1.getText().toString();
                String fullname = ed2.getText().toString();
                String password = ed3.getText().toString();
                if (passwordIsValid && emailIsValid && fullNameIsValid) {
                    service.register(new User(0, email, null, fullname, password)).enqueue(registerCallback);
                } else {
                    if (!passwordIsValid) {
                        passwordError.setErrorEnabled(true);
                        passwordError.setError("Password must be more than 6 characters");
                    }
                    if (!email.matches(regex)) {
                        emailError.setErrorEnabled(true);
                        emailError.setError("Email is valid");
                    }
                    if (!fullNameIsValid) {
                        fullNameError.setErrorEnabled(true);
                        fullNameError.setError("Full name is require");
                    }
                }

            }
        });

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(intent);
            }
        });
    }

    Callback<String> registerCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                Toast.makeText(SignUpScreen.this, "Sign up success", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (response.code() == 401) {
                Toast.makeText(SignUpScreen.this, "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    };

}