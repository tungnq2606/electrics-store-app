package com.example.myapplication.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class EnterEmail extends AppCompatActivity {
    private RelativeLayout send;
    private ImageView back;
    private TextView notAccount;
    private IRetrofitService service;
    private TextInputEditText ed;
    private TextInputLayout emailError;
    private String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]{0,10})*@[A-Za-z0-9]+(\\.[A-Za-z0-9]{0,10})*(\\.[A-Za-z]{0,5})$";
    private Boolean emailIsValid = false;
    private ProgressBar progressBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);
        back = findViewById(R.id.resetPwBackBtn);
        emailError = findViewById(R.id.emailErrorTF);
        notAccount = findViewById(R.id.accountValid);
        send = findViewById(R.id.sendEmailButton);
        service = RetrofitBuilder.createServices(IRetrofitService.class);
        ed = findViewById(R.id.resetEmail);
        progressBar = findViewById(R.id.sendEmailProgress);
        notAccount.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches(regex)) {
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setEnabled(false);
                String email = ed.getText().toString().trim();
                if (emailIsValid) {
                    progressBar.setVisibility(View.VISIBLE);
                    service.forgotPassword(new Account(email,null)).enqueue(sendEmailCallback);
                } else {
                    emailError.setErrorEnabled(true);
                    emailError.setError("Email is valid");
                }
            }
        });
    }

    Callback<String> sendEmailCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            if (response.isSuccessful()) {
                send.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(EnterEmail.this, SendEmailSuccess.class);
                startActivity(intent);
            } else {
                send.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                notAccount.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    };
}