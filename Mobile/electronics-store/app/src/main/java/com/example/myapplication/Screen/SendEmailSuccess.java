package com.example.myapplication.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SendEmailSuccess extends AppCompatActivity {
    Button goToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_success);
        goToSignIn = findViewById(R.id.goToSignIn2);

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendEmailSuccess.this, SignInScreen.class);
                intent.putExtra("Started", "SendEmail");
                startActivity(intent);
            }
        });
    }
}