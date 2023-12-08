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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class PersonalCar_Tab extends AppCompatActivity {
    NumberPicker manufactureYear;
    ImageButton personalCar_backBtn;

    Spinner mileType,consumptionType,manufacturerType;
    EditText mileage,consumption,model,personalCarResult;

    Button carFootprintBtn;

    CarbonHelper DB;
    private static final double PETROL_EMISSION_FACTOR = 230; // Placeholder value
    private static final double DIESEL_EMISSION_FACTOR = 270; // Placeholder value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_car_tab);
        manufactureYear = findViewById(R.id.manufactureYear);
        personalCar_backBtn = findViewById(R.id.personalCar_backBtn);
        mileType = findViewById(R.id.mileType);
        mileage=findViewById(R.id.mileage);
        consumptionType=findViewById(R.id.consumptionType);
        consumption=findViewById(R.id.consumption);
        manufacturerType=findViewById(R.id.manufacturerType);
        model=findViewById(R.id.model);
        personalCarResult=findViewById(R.id.personalCarResult);
        carFootprintBtn=findViewById(R.id.carFootprintBtn);

        DB=new CarbonHelper(this);

        personalCar_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalMainPage.class);
                startActivity(intent);
            }
        });

        manufactureYear.setMinValue(1999);
        manufactureYear.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));

        manufactureYear.setValue(Calendar.getInstance().get(Calendar.YEAR));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unit_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mileType.setAdapter(adapter);

        mileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected unit (KM or Miles)
                String selectedUnit = parentView.getItemAtPosition(position).toString();
                Toast.makeText(PersonalCar_Tab.this, "Selected Unit: " + selectedUnit, Toast.LENGTH_SHORT).show();

                if (selectedUnit.equals("Kilometers (KM)")) {
                    // Handle KM logic
                    mileage.setHint("Enter mileage in KM");
                } else {
                    // Handle Miles logic
                    mileage.setHint("Enter mileage in Miles");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> consumptionAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.consumption_array,
                android.R.layout.simple_spinner_item
        );
        consumptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        consumptionType.setAdapter(consumptionAdapter);

        consumptionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedConsumption = parentView.getItemAtPosition(position).toString();
                Toast.makeText(PersonalCar_Tab.this, "Selected Consumption: " + selectedConsumption, Toast.LENGTH_SHORT).show();
                // Add your logic for handling selected consumption type here
                if (selectedConsumption.equals("Petrol")) {
                    // Handle KM logic
                    consumption.setHint("Enter consumption in Petrol");
                } else {
                    // Handle Miles logic
                    consumption.setHint("Enter consumption in Diesel");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<CharSequence> manufacturerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.manufacturer_array,
                android.R.layout.simple_spinner_item
        );
        manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        manufacturerType.setAdapter(manufacturerAdapter);

        manufacturerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedManufacturer = parentView.getItemAtPosition(position).toString();
                Toast.makeText(PersonalCar_Tab.this, "Selected Manufacturer: " + selectedManufacturer, Toast.LENGTH_SHORT).show();
                // Add your logic for handling selected manufacturer here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        carFootprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCarbonEmission();
            }
        });
    }

    private void calculateCarbonEmission() {
        try {
            Intent intent = getIntent();
            String loggedInUser= intent.getStringExtra("a2");
            double userMileage = Double.parseDouble(mileage.getText().toString());
            double userConsumption = Double.parseDouble(consumption.getText().toString());
            String manufacturer=manufacturerType.getSelectedItem().toString();
            String modelType=model.getText().toString();
            String consumptionT=consumptionType.getSelectedItem().toString();

            // Convert mileage to kilometers if the selected unit is "Miles"
            if (mileType.getSelectedItem().toString().equals("Miles")) {
                // 1 mile is approximately 1.60934 kilometers
                userMileage *= 1.60934;
            }

            // Convert consumption to liters per kilometer (L/km)
            double consumptionRate = userConsumption / 100;

            double emissionFactor;

            // Choose emission factor based on selected consumption type
            if (consumptionType.getSelectedItem().toString().equals("Petrol")) {
                emissionFactor = PETROL_EMISSION_FACTOR;
            } else {
                emissionFactor = DIESEL_EMISSION_FACTOR;
            }

            double carbonEmission = userMileage * consumptionRate * emissionFactor;
            personalCarResult.setText("Estimated Carbon Emission: " + carbonEmission + " g CO2");
            Boolean checkInsertPersonalCarEmission=DB.insertPersonalCarEmission(loggedInUser, userMileage, consumptionT,userConsumption, manufacturer, modelType, carbonEmission);
            if(checkInsertPersonalCarEmission==true)
            {

                Toast.makeText(PersonalCar_Tab.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mileage.setText("");
                        personalCarResult.setText("");
                        model.setText("");
                        consumption.setText("");
                        manufacturerType.setSelection(0); // reset Spinner to the first item
                        mileType.setSelection(0);
                        consumptionType.setSelection(0);
                    }
                }, 10000);
            }
            else
            {
                Toast.makeText(PersonalCar_Tab.this,"New Entry Not Inserted",Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid values for mileage and consumption", Toast.LENGTH_SHORT).show();
        }
    }

}

