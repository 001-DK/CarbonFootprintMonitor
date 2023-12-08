package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {
    ImageButton backToLog,back1;

    Button createBtn;

    EditText pName,email,county,city,phone,pass,cpass;

    String fullName,pEmail,pCounty,pCity,pPhone,pPass,pCpass;

    CarbonHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        back1=findViewById(R.id.back1);
        pName=findViewById(R.id.pName);
        email=findViewById(R.id.email);
        county=findViewById(R.id.county);
        city=findViewById(R.id.city);
        phone=findViewById(R.id.phone);
        pass=findViewById(R.id.pass);
        cpass=findViewById(R.id.cpass);
        createBtn=findViewById(R.id.createBtn);
        DB=new CarbonHelper(this);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName=pName.getText().toString();
                pEmail=email.getText().toString();
                pCounty=county.getText().toString();
                pCity=city.getText().toString();
                pPhone=phone.getText().toString();
                pPass=pass.getText().toString();
                pCpass=cpass.getText().toString();
                //Ensuring all fields are filled
                String[] fields = {fullName, pEmail, pCounty, pCity, pPhone, pPass, pCpass};

                for (String field : fields) {
                    if (TextUtils.isEmpty(field)) {
                        Toast.makeText(CreateAccount.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        return; // Stop further processing if any field is empty
                    }}
                if (!pPass.equals(pCpass))
                {
                    Toast.makeText(CreateAccount.this,"Password does not match",Toast.LENGTH_SHORT).show();
                }

                Cursor cursor=DB.checkData(pEmail);
                if(cursor.getCount()>0)
                {
                    Toast.makeText(CreateAccount.this, "A record with this email already exists", Toast.LENGTH_SHORT).show();
                    pName.setText("");
                    email.setText("");
                    county.setText("");
                    city.setText("");
                    phone.setText("");
                    pass.setText("");
                    cpass.setText("");
                }
                else
                {
                    Boolean checkInsertPerson=DB.insertPersonalUser(pEmail,fullName,pCounty,pCity,pPhone,pPass);
                    if(checkInsertPerson==true)
                    {

                        Toast.makeText(CreateAccount.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();
                        pName.setText("");
                        email.setText("");
                        county.setText("");
                        city.setText("");
                        phone.setText("");
                        pass.setText("");
                        cpass.setText("");
                    }
                    else
                    {
                        Toast.makeText(CreateAccount.this,"New Entry Not Inserted",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginPage.class);

                startActivity(intent);
            }
        });
    }
}