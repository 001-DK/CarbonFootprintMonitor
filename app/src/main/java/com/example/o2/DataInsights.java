package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DataInsights extends AppCompatActivity {

    ImageButton dataInsights_backBtn;


    TextView houseHoldTotal,personalCarTotal,flightTotal,busRailTotal,Total,
            recentHouseHoldTotal,recentPersonalCarTotal,recentFlightTotal,recentBusRailTotal,recentTotal,userFact,comparison;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_insights);

        dataInsights_backBtn=findViewById(R.id.dataInsights_backBtn);
        houseHoldTotal=findViewById(R.id.houseHoldTotal);
        personalCarTotal=findViewById(R.id.personalCarTotal);
        flightTotal=findViewById(R.id.flightTotal);
        busRailTotal=findViewById(R.id.busRailTotal);
        Total=findViewById(R.id.Total);

        recentHouseHoldTotal=findViewById(R.id.recentHouseHoldTotal);
        recentPersonalCarTotal=findViewById(R.id.recentPersonalCarTotal);
        recentFlightTotal=findViewById(R.id.recentFlightTotal);
        recentBusRailTotal=findViewById(R.id.recentBusRailTotal);
        recentTotal=findViewById(R.id.recentTotal);
        userFact=findViewById(R.id.userFact);
        comparison=findViewById(R.id.comparison);

        Intent intent = getIntent();
        String loggedInUser= intent.getStringExtra("a2");

        DecimalFormat df = new DecimalFormat("0,000.0");

        // Retrieve and set emissions from HouseholdEmission table
        Double houseHoldEmission = getEmissionTotalForTable("HouseholdEmission", loggedInUser);
        houseHoldTotal.setText(df.format(houseHoldEmission));

        // Retrieve and set emissions from PersonalCarEmission table
        Double personalCarEmission = getEmissionTotalForTable("PersonalCarEmission", loggedInUser);
        personalCarTotal.setText(df.format(personalCarEmission));

        // Retrieve and set emissions from FlightsEmission table
        Double flightEmission = getEmissionTotalForTable("FlightsEmission", loggedInUser);
        flightTotal.setText(df.format(flightEmission));

        // Retrieve and set emissions from BusRailEmission table
        Double busRailEmission = getEmissionTotalForTable("BusRailEmission", loggedInUser);
        busRailTotal.setText(df.format(busRailEmission));

        // Calculate the total emissions for the logged-in user
        Double totalEmission = houseHoldEmission + personalCarEmission + flightEmission + busRailEmission;
        Total.setText(df.format(totalEmission));

        Double recentHouseHoldEmission = getRecentEmissionTotalForTable("HouseholdEmission", loggedInUser);
        recentHouseHoldTotal.setText(df.format(recentHouseHoldEmission));

        // Retrieve and set total emissions from the most recent record in PersonalCarEmission table
        Double recentPersonalCarEmission = getRecentEmissionTotalForTable("PersonalCarEmission", loggedInUser);
        recentPersonalCarTotal.setText(df.format(recentPersonalCarEmission));

        // Retrieve and set total emissions from the most recent record in FlightsEmission table
        Double recentFlightEmission = getRecentEmissionTotalForTable("FlightsEmission", loggedInUser);
        recentFlightTotal.setText(df.format(recentFlightEmission));

        // Retrieve and set total emissions from the most recent record in BusRailEmission table
        Double recentBusRailEmission = getRecentEmissionTotalForTable("BusRailEmission", loggedInUser);
        recentBusRailTotal.setText(df.format(recentBusRailEmission));

        // Calculate the total emissions for the logged-in user based on the most recent records
        Double recentTotalEmission = recentHouseHoldEmission + recentPersonalCarEmission + recentFlightEmission + recentBusRailEmission;
        recentTotal.setText(df.format(recentTotalEmission));

        double averageEmissionInKenya = 0.3;

// Convert total emissions to metric tons
        double totalEmissionInMetricTons = totalEmission / 1000;

// Output a statement based on the comparison
        String emissionStatement = "";
        if (totalEmissionInMetricTons < averageEmissionInKenya) {
            emissionStatement = "Congratulations! Your average carbon emission is  " +df.format(totalEmissionInMetricTons) + " metric tones,  below the national average in Kenya.";
        } else if (totalEmissionInMetricTons > averageEmissionInKenya) {
            emissionStatement = "Your average carbon emission is " +df.format(totalEmissionInMetricTons) +" metric tones, higher than the national average in Kenya. Consider reducing your carbon footprint.";
        } else {
            emissionStatement = "Your average carbon emission is  " +df.format(totalEmissionInMetricTons) +" metric tones,  in line with the national average in Kenya.";
        }

        userFact.setText(emissionStatement);

        String comparisonStatement = "";
        if (recentTotalEmission > totalEmission) {
            comparisonStatement = "Your most recent total carbon emission is higher than your previous total emission. Keep up the efforts to reduce your carbon footprint!";
        } else if (recentTotalEmission < totalEmission) {
            comparisonStatement = "Congratulations! Your most recent total carbon emission is lower than your previous total emission. You're making progress in reducing your carbon footprint!";
        } else {
            comparisonStatement = "Your most recent total carbon emission is the same as your previous total emission. Keep tracking your carbon footprint to make a positive impact!";
        }
        // Set the comparison statement to the TextView
        comparison.setText(comparisonStatement);

        dataInsights_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PersonalMainPage.class);

                startActivity(intent);
            }
        });

    }
    private double getEmissionTotalForTable(String tableName, String userEmail) {
        CarbonHelper DB = new CarbonHelper(this);

        // Query to retrieve emissions for the logged-in user from the specified table
        String query = "SELECT SUM(TotalEmission) FROM " + tableName + " WHERE PersonalEmail = ?";
        SQLiteDatabase database = DB.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, new String[]{userEmail});

        double totalEmission = 0;

        if (cursor.moveToFirst()) {
            totalEmission = cursor.getDouble(0);
        }

        // Close the cursor and database after use
        cursor.close();
        database.close();

        return totalEmission;
    }
    private double getRecentEmissionTotalForTable(String tableName, String userEmail) {
        CarbonHelper DB = new CarbonHelper(this);

        // Query to retrieve total emissions from the most recent record for the logged-in user
        String query = "SELECT TotalEmission FROM " + tableName +
                " WHERE PersonalEmail = ? ORDER BY ID DESC LIMIT 1";

        SQLiteDatabase database = DB.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, new String[]{userEmail});

        double recentTotalEmission = 0;

        if (cursor.moveToFirst()) {
            recentTotalEmission = cursor.getDouble(0);
        }

        // Close the cursor and database after use
        cursor.close();
        database.close();

        return recentTotalEmission;
    }
}