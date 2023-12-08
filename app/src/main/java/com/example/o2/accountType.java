package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class accountType extends AppCompatActivity {

    Button personalBtn, businessBtn;
    ImageButton back2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        personalBtn=findViewById(R.id.personalBtn);
        businessBtn=findViewById(R.id.businessBtn);
        back2=findViewById(R.id.back2);

        personalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CreateAccount.class);

                startActivity(intent);
            }
        });
        businessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),BusinessCreateAccount.class);

                startActivity(intent);
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginPage.class);

                startActivity(intent);
            }
        });
    }
}