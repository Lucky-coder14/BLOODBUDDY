package com.example.bloodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegisterationActivity extends AppCompatActivity {
    private Button donorButton ,recipientButton;
    private TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registeration);
        donorButton = findViewById(R.id.donorButton);
        backButton = findViewById(R.id.backButton);
        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegisterationActivity.this,DonorRegisterationActivity.class) ;
                startActivity(intent);
            }
        });
        recipientButton = findViewById(R.id.recipientButton);
        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(SelectRegisterationActivity.this,RecipientRegistrationActivity.class) ;
              startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegisterationActivity.this,LoginActivity.class) ;
                startActivity(intent);
            }
        });
    }
}