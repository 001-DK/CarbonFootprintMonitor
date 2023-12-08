package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    Button loginBtn;

    TextView signTxt;

    EditText userEmail,password;

    CarbonHelper DB;

    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        DB= new CarbonHelper(this);
        loginBtn=findViewById(R.id.loginBtn);
        signTxt=findViewById(R.id.signTxt);

        userEmail=findViewById(R.id.userEmail);
        password=findViewById(R.id.password);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=userEmail.getText().toString().trim();
                pass=password.getText().toString().trim();

                Log.d("Login", "Email: " + email);
                Log.d("Login", "Password: " + pass);

                Cursor res=DB.getUser(email,pass);
                if(res.getCount()>0)
                {
                    Intent intent=new Intent(getApplicationContext(), PersonalMainPage.class);
                    intent.putExtra("a1",email);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginPage.this, "Login Failed. Check your email or password", Toast.LENGTH_SHORT).show();
                }
                res.close();
            }
        });

        signTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), accountType.class);

                startActivity(intent);
            }
        });

    }
}