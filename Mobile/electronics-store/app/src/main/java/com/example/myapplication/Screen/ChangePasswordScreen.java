package com.example.myapplication.Screen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.Account;
import com.example.myapplication.Myretrofit.IRetrofitService;
import com.example.myapplication.Myretrofit.RetrofitBuilder;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordScreen extends AppCompatActivity {
    private TextInputEditText ed1, ed2, ed3;
    private ImageView back;
    private Button change;
    private IRetrofitService service;
    TextInputLayout oldPwError, newPwError, confirmPwError;
    private boolean oldPwIsValid = false;
    private boolean newPwIsValid = false;
    private boolean confirmPwIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);
        back = findViewById(R.id.changePasswordBackBtn);
        ed1 = findViewById(R.id.oldPassword);
        ed2 = findViewById(R.id.newChangePassword);
        ed3 = findViewById(R.id.confirmPassword);
        change = findViewById(R.id.changePasswordButton);
        oldPwError = (TextInputLayout) findViewById(R.id.oldPasswordTF);
        newPwError = findViewById(R.id.newChangePasswordTF);
        confirmPwError = findViewById(R.id.confirmPasswordTF);
        service = RetrofitBuilder.createServices(IRetrofitService.class);

//        Validation form
        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6) {
                    oldPwError.setErrorEnabled(true);
                    oldPwError.setError("Old password must be more than 6 characters");
                    oldPwIsValid = false;
                } else {
                    oldPwError.setErrorEnabled(false);
                    oldPwError.setError(null);
                    oldPwIsValid = true;
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
                if (s.toString().trim().length() < 6) {
                    newPwError.setErrorEnabled(true);
                    newPwError.setError("New password must be more than 6 characters");
                    newPwIsValid = false;
                } else {
                    newPwError.setErrorEnabled(false);
                    newPwError.setError(null);
                    newPwIsValid = true;
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
                if (newPwIsValid) {
                    if (!s.toString().equals(ed2.getText().toString())) {
                        confirmPwError.setErrorEnabled(true);
                        confirmPwError.setError("Password and confirm password incorrect");
                        confirmPwIsValid = false;
                    } else {
                        confirmPwError.setErrorEnabled(false);
                        confirmPwError.setError(null);
                        confirmPwIsValid = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = ed1.getText().toString().trim();
                String newPassword = ed2.getText().toString().trim();
                String email = getIntent().getStringExtra("email").trim();
                if (confirmPwIsValid && oldPwIsValid && newPwIsValid) {
                    service.changePassword(new Account(email, oldPassword, newPassword)).enqueue(changePasswordCallback);
                } else {
                    if (!confirmPwIsValid) {
                        confirmPwError.setErrorEnabled(true);
                        confirmPwError.setError("Password and confirm password incorrect");
                    }
                    if (!newPwIsValid) {
                        newPwError.setErrorEnabled(true);
                        newPwError.setError("New password must be more than 6 characters");
                    }
                    if (!oldPwIsValid) {
                        oldPwError.setErrorEnabled(true);
                        oldPwError.setError("Old password must be more than 6 characters");
                    }
                }
            }
        });
//
    }

    Callback<String> changePasswordCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                Toast.makeText(ChangePasswordScreen.this, "Your password was changed", Toast.LENGTH_SHORT).show();
            }
            if (response.code() == 401) {
                Toast.makeText(ChangePasswordScreen.this, "Failed to change", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    };
}