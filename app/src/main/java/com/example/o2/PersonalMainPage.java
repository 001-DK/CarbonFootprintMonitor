package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PersonalMainPage extends AppCompatActivity {

    ImageView houseImg,personalFlight,personalCarImg,log_out,insightsTab,accountTab,personalBus;

    FrameLayout insightsFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_main_page);

        log_out=findViewById(R.id.log_out);
        houseImg=findViewById(R.id.houseImg);
        personalFlight=findViewById(R.id.personalFlight);
        personalCarImg=findViewById(R.id.personalCarImg);
        insightsTab=findViewById(R.id.insightsTab);
        accountTab=findViewById(R.id.accountTab);
        personalBus=findViewById(R.id.personalBus);



        Intent intent = getIntent();
        String loggedInUser= intent.getStringExtra("a1");
        houseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HouseHold_Tab.class);
                intent.putExtra("a2",loggedInUser);
                startActivity(intent);
            }
        });
        insightsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DataInsights.class);
                intent.putExtra("a2",loggedInUser);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
        personalCarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalCar_Tab.class);
                intent.putExtra("a2",loggedInUser);
                startActivity(intent);
            }
        });
        personalFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), flights_tab.class);
                intent.putExtra("a2",loggedInUser);
                startActivity(intent);
            }
        });
        personalBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BusRail_Tab.class);
                intent.putExtra("a2",loggedInUser);
                startActivity(intent);
            }
        });
    }
}