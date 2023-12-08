package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class flights_tab extends AppCompatActivity {
    ImageButton flights_backBtn;

    RadioButton returnRbtn,onewayRbtn;
    EditText from,to,tripsNo,result;
    Spinner classType;

    Button flightEmissionBtn;
    CarbonHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_tab);

        flights_backBtn=findViewById(R.id.flights_backBtn);
        returnRbtn=findViewById(R.id.returnRbtn);
        onewayRbtn=findViewById(R.id.onewayRbtn);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        tripsNo=findViewById(R.id.tripsNo);
        result=findViewById(R.id.result);
        classType=findViewById(R.id.classType);
        flightEmissionBtn=findViewById(R.id.flightEmissionBtn);
        DB=new CarbonHelper(this);

        String[] flightClasses = {"Premium", "First Class", "Business", "Economy", "Average"};

        ArrayAdapter<String> flight_adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,flightClasses);

        flight_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classType.setAdapter(flight_adapter);

        classType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass = parent.getItemAtPosition(position).toString();
                Toast.makeText(flights_tab.this, "Selected Class: " + selectedClass, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        flightEmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateFlightEmission();
            }
        });


        flights_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PersonalMainPage.class);

                startActivity(intent);
            }
        });
    }
    private double getEmissionFactorForClass(String selectedClass) {
        switch (selectedClass) {
            case "Premium":
                return 10.0;
            case "First Class":
                return 8.0;
            case "Business":
                return 6.0;
            case "Economy":
                return 4.0;
            case "Average":
                return 5.0;
            default:
                return 0.0;
        }
    }

    private void calculateFlightEmission() {
        Intent intent = getIntent();
        String loggedInUser= intent.getStringExtra("a2");
        String selectedClass = classType.getSelectedItem().toString();
        String source = from.getText().toString();
        String destination = to.getText().toString();
        int numberOfTrips = Integer.parseInt(tripsNo.getText().toString());

        // Get the distance between the two locations
        double distance = LocationDistanceDatabase.getDistance(source, destination);

        if (distance != -1) {
            // Sample calculation based on distance and number of trips
            double emissionFactor = getEmissionFactorForClass(classType.getSelectedItem().toString());

            // Adjust emission calculation based on return or one-way
            double totalEmission;
            if (returnRbtn.isChecked()) {
                // Assuming a return trip, multiply the distance by 2
                totalEmission = distance * 2 * emissionFactor * numberOfTrips;
            } else {
                // Assuming a one-way trip
                totalEmission = distance * emissionFactor * numberOfTrips;
            }
            result.setText("Estimated Carbon Emission: " +String.valueOf(totalEmission)+ " g CO2");

            Boolean checkInsertFlightEmission=DB.insertFlightsEmission(loggedInUser,source, destination, numberOfTrips, selectedClass, totalEmission);
            if(checkInsertFlightEmission==true)
            {

                Toast.makeText(flights_tab.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        from.setText("");
                        to.setText("");
                        tripsNo.setText("");
                        result.setText("");
                        returnRbtn.setChecked(false); // reset RadioButton
                        onewayRbtn.setChecked(false); // reset RadioButton
                        classType.setSelection(0); // reset Spinner to the first item
                    }
                }, 5000);
            }
            else
            {
                Toast.makeText(flights_tab.this,"New Entry Not Inserted",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Distance not found for the given locations", Toast.LENGTH_SHORT).show();
        }
    }
}