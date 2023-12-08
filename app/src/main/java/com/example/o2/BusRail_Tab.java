package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class BusRail_Tab extends AppCompatActivity {
    ImageButton bus_rail_backBtn;
    Button bus_railFootprintBtn;
    EditText busDistance,coachDistance,sgrDistance,cabDistance,bus_RailResult;

    Spinner bus_DistanceType,coachDistanceType,sgrDistanceType,cabDistanceType;
    CarbonHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_rail_tab);
        bus_rail_backBtn=findViewById(R.id.bus_rail_backBtn);
        bus_railFootprintBtn=findViewById(R.id.bus_railFootprintBtn);

        busDistance=findViewById(R.id.busDistance);
        coachDistance=findViewById(R.id.coachDistance);
        sgrDistance=findViewById(R.id.sgrDistance);
        cabDistance=findViewById(R.id.cabDistance);

        bus_RailResult=findViewById(R.id.bus_RailResult);

        bus_DistanceType=findViewById(R.id.bus_DistanceType);
        coachDistanceType=findViewById(R.id.coachDistanceType);
        sgrDistanceType=findViewById(R.id.sgrDistanceType);
        cabDistanceType=findViewById(R.id.cabDistanceType);
        DB=new CarbonHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bus_DistanceType.setAdapter(adapter);
        bus_DistanceType.setOnItemSelectedListener(new DistanceTypeItemSelectedListener(busDistance));

        coachDistanceType.setAdapter(adapter);
        coachDistanceType.setOnItemSelectedListener(new DistanceTypeItemSelectedListener(coachDistance));

        sgrDistanceType.setAdapter(adapter);
        sgrDistanceType.setOnItemSelectedListener(new DistanceTypeItemSelectedListener(sgrDistance));

        cabDistanceType.setAdapter(adapter);
        cabDistanceType.setOnItemSelectedListener(new DistanceTypeItemSelectedListener(cabDistance));

        bus_rail_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalMainPage.class);

                startActivity(intent);
            }
        });

        bus_railFootprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayFootprint();
            }
        });
    }



    private static class DistanceTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private final EditText distanceEditText;
        public DistanceTypeItemSelectedListener(EditText distanceEditText) {
            this.distanceEditText = distanceEditText;
        }
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            String selectedUnit = parentView.getSelectedItem().toString();
            // Update the corresponding EditText based on the selected item
            if ("Kilometers(KM)".equals(selectedUnit)) {
                distanceEditText.setHint("Distance in KM");
            } else if ("Miles".equals(selectedUnit)) {
                distanceEditText.setHint("Distance in Miles");
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // Do nothing here
        }
    }
    private void calculateAndDisplayFootprint()
    {
        try {
            Intent intent = getIntent();
            String loggedInUser= intent.getStringExtra("a2");

            double busDistanceValue = Double.parseDouble(busDistance.getText().toString());
            double coachDistanceValue = Double.parseDouble(coachDistance.getText().toString());
            double sgrDistanceValue = Double.parseDouble(sgrDistance.getText().toString());
            double cabDistanceValue = Double.parseDouble(cabDistance.getText().toString());

            double carbonFootprint = calculateCarbonFootprint(busDistanceValue, coachDistanceValue, sgrDistanceValue, cabDistanceValue);

            bus_RailResult.setText(String.format("Carbon Footprint: %.2f", carbonFootprint));
            Boolean checkInsertBusRailEmission=DB.insertBusRailEmission(loggedInUser, busDistanceValue, coachDistanceValue, sgrDistanceValue, cabDistanceValue, carbonFootprint);
            if(checkInsertBusRailEmission==true)
            {

                Toast.makeText(BusRail_Tab.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        busDistance.setText("");
                        coachDistance.setText("");
                        sgrDistance.setText("");
                        cabDistance.setText("");
                        bus_RailResult.setText("");
                        bus_DistanceType.setSelection(0); // reset Spinner to the first item
                        coachDistanceType.setSelection(0);
                        sgrDistanceType.setSelection(0);
                        cabDistanceType.setSelection(0);
                    }
                }, 5000);
            }
            else
            {
                Toast.makeText(BusRail_Tab.this,"New Entry Not Inserted",Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric values)
            Toast.makeText(this, "Invalid input. Please enter numeric values for distances.", Toast.LENGTH_SHORT).show();
        }


    }

    private double calculateCarbonFootprint(double busDistance, double coachDistance, double sgrDistance, double cabDistance) {
        // Simple example: Assume a constant carbon footprint value for each distance
        double busCarbonFootprint = busDistance * 0.1;  // Adjust the constant factor as needed
        double coachCarbonFootprint = coachDistance * 0.05;
        double sgrCarbonFootprint = sgrDistance * 0.08;
        double cabCarbonFootprint = cabDistance * 0.2;

        // Sum up the carbon footprints for each mode of transportation
        return busCarbonFootprint + coachCarbonFootprint + sgrCarbonFootprint + cabCarbonFootprint;
    }
}