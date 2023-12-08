package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class HouseHold_Tab extends AppCompatActivity {
    ImageButton houseBackBtn;

    Button houseHoldBtn;

    EditText electricityBill,cookingGasAmt,heatingOilAmt,propaneAmt,woodenPalletAmt,lpgAmt,houseEmission;

    double electricityConversionFactor = 0.12; // replace with your actual conversion factor
    double cookingGasConversionFactor = 2.5; // replace with your actual conversion factor
    double heatingOilConversionFactor = 1.8; // replace with your actual conversion factor
    double propaneConversionFactor = 2.0; // replace with your actual conversion factor
    double woodenPalletConversionFactor = 0.05; // replace with your actual conversion factor
    double lpgConversionFactor = 1.0; // replace with your actual conversion factor
    CarbonHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_hold_tab);
        houseBackBtn=findViewById(R.id.houseBackBtn);
        electricityBill=findViewById(R.id.electricityBill);
        cookingGasAmt=findViewById(R.id.cookingGasAmt);
        heatingOilAmt=findViewById(R.id.heatingOilAmt);
        propaneAmt=findViewById(R.id.propaneAmt);
        woodenPalletAmt=findViewById(R.id.woodenPalletAmt);
        lpgAmt=findViewById(R.id.lpgAmt);
        houseEmission=findViewById(R.id.houseEmission);
        houseHoldBtn=findViewById(R.id.houseHoldBtn);

        DB=new CarbonHelper(this);
        houseBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PersonalMainPage.class);
                startActivity(intent);
            }
        });

        houseHoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateHouseholdEmission();
            }
        });
    }

    private void calculateHouseholdEmission() {
        try {
            Intent intent = getIntent();
            String loggedInUser= intent.getStringExtra("a2");
            // Extract user inputs (amount spent)
            double electricitySpent = Double.parseDouble(electricityBill.getText().toString());
            double cookingGasSpent = Double.parseDouble(cookingGasAmt.getText().toString());
            double heatingOilSpent = Double.parseDouble(heatingOilAmt.getText().toString());
            double propaneSpent = Double.parseDouble(propaneAmt.getText().toString());
            double woodenPalletSpent = Double.parseDouble(woodenPalletAmt.getText().toString());
            double lpgSpent = Double.parseDouble(lpgAmt.getText().toString());

            // Calculate estimated amounts based on conversion factors
            double electricityAmount = electricitySpent / electricityConversionFactor;
            double cookingGasAmount = cookingGasSpent / cookingGasConversionFactor;
            double heatingOilAmount = heatingOilSpent / heatingOilConversionFactor;
            double propaneAmount = propaneSpent / propaneConversionFactor;
            double woodenPalletAmount = woodenPalletSpent / woodenPalletConversionFactor;
            double lpgAmount = lpgSpent / lpgConversionFactor;

            // Add your calculation logic here based on estimated amounts

            // For example, calculate the total emission by adding all sources
            double totalEmission = electricityAmount + cookingGasAmount + heatingOilAmount + propaneAmount + woodenPalletAmount + lpgAmount;
            // Insert HouseHoldEmission data
            Boolean checkInsertHouseHoldEmission=DB.insertHouseholdEmission(loggedInUser, electricitySpent, cookingGasSpent, heatingOilSpent,
                    propaneSpent, woodenPalletSpent, lpgSpent, totalEmission);
            if(checkInsertHouseHoldEmission==true)
            {

                Toast.makeText(HouseHold_Tab.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        electricityBill.setText("");
                        cookingGasAmt.setText("");
                        heatingOilAmt.setText("");
                        propaneAmt.setText("");
                        woodenPalletAmt.setText("");
                        lpgAmt.setText("");
                        houseEmission.setText("");
                    }
                }, 5000);
            }
            else
            {
                Toast.makeText(HouseHold_Tab.this,"New Entry Not Inserted",Toast.LENGTH_SHORT).show();
            }


            // Display the result in the houseEmission EditText
            houseEmission.setText("Total Household Emission: " + totalEmission + " g CO2");
        } catch (NumberFormatException e) {
            // Handle invalid input format
            houseEmission.setText("Invalid input. Please enter valid numbers.");
        }
    }
}