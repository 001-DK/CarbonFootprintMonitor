package com.example.o2;

public class LocationDistanceDatabase {
    // Define your locations and distances here
    private static final String[][] locationsAndDistances = {
            {"Nairobi", "Kisumu", "264.48"},
            {"Nairobi", "Mombasa", "432"},
            // Add more locations and distances as needed
    };

    public static double getDistance(String from, String to) {
        for (String[] locationAndDistance : locationsAndDistances) {
            String source = locationAndDistance[0];
            String destination = locationAndDistance[1];
            double distance = Double.parseDouble(locationAndDistance[2]);

            if (from.equals(source) && to.equals(destination)) {
                return distance;
            }
        }
        return -1; // Return -1 if no matching distance is found
    }
}
