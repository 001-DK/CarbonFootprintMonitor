package com.example.o2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class CarbonHelper extends SQLiteOpenHelper {
    public CarbonHelper(Context context){super(context, "CarbonFootPrint.db", null, 1);}
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table PersonalAccounts(PersonalEmail Text primary key,PersonalName Text,PersonalCounty Text, PersonalCity Text,PersonalPhone Text,PersonalPass Text)");
        DB.execSQL("create table BusRailEmission(ID INTEGER PRIMARY KEY AUTOINCREMENT, PersonalEmail Text, BusDistance Double, CoachDistance Double, SgrDistance Double, CabDistance Double, TotalEmission Double, FOREIGN KEY (PersonalEmail) REFERENCES PersonalAccounts(PersonalEmail))");
        DB.execSQL("create table PersonalCarEmission(ID INTEGER PRIMARY KEY AUTOINCREMENT, PersonalEmail Text, Mileage Double, ConsumptionType Text, Consumption Double, Manufacturer Text, Model Text, TotalEmission Double, FOREIGN KEY (PersonalEmail) REFERENCES PersonalAccounts(PersonalEmail))");
        DB.execSQL("create table HouseholdEmission(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PersonalEmail TEXT," +  // Foreign key referencing PersonalAccounts
                "ElectricitySpent REAL," +
                "CookingGasSpent REAL," +
                "HeatingOilSpent REAL," +
                "PropaneSpent REAL," +
                "WoodenPalletSpent REAL," +
                "LpgSpent REAL," +
                "TotalEmission REAL," +
                "FOREIGN KEY (PersonalEmail) REFERENCES PersonalAccounts(PersonalEmail))");
        DB.execSQL("create table FlightsEmission(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PersonalEmail TEXT," +  // Foreign key referencing PersonalAccounts
                "Source TEXT," +
                "Destination TEXT," +
                "NumberOfTrips INTEGER," +
                "ClassType TEXT," +
                "TotalEmission REAL," +
                "FOREIGN KEY (PersonalEmail) REFERENCES PersonalAccounts(PersonalEmail))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists PersonalAccounts");
        DB.execSQL("drop Table if exists PersonalCarEmission");
        DB.execSQL("drop Table if exists BusRailEmission");
        DB.execSQL("drop Table if exists HouseholdEmission");
        DB.execSQL("drop Table if exists FlightsEmission");



    }

    public Boolean insertPersonalUser(String PersonalEmail, String PersonalName,String PersonalCounty,String PersonalCity,String PersonalPhone, String PersonalPass)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("PersonalEmail",PersonalEmail);
        content.put("PersonalName",PersonalName);
        content.put("PersonalCounty",PersonalCounty);
        content.put("PersonalCity",PersonalCity);
        content.put("PersonalPhone",PersonalPhone);
        content.put("PersonalPass",PersonalPass);

        long result= DB.insert("PersonalAccounts",null,content);
        DB.close();
        if (result==-1)
        {
            throw new SQLException("Failed to insert row");
        }
        else
        {
            return true;
        }
    }
    public Cursor checkData(String PersonalEmail)
    {
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("SELECT * FROM PersonalAccounts WHERE PersonalEmail=?", new String[]{PersonalEmail});
        return cursor;
    }
    public Cursor getUser(String PersonalEmail, String PersonalPass)
    {
        SQLiteDatabase DB=this.getWritableDatabase();

        Log.d("GetUser", "Email: " + PersonalEmail);
        Log.d("GetUser", "Password: " + PersonalPass);

        Cursor res=DB.rawQuery("SELECT * FROM PersonalAccounts WHERE PersonalEmail=? AND PersonalPass=?",new String[]{PersonalEmail,PersonalPass});
        return res;
    }

    public Boolean insertHouseholdEmission(String personalEmail, double electricitySpent, double cookingGasSpent,
                                           double heatingOilSpent, double propaneSpent,
                                           double woodenPalletSpent, double lpgSpent,
                                           double totalEmission) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("PersonalEmail", personalEmail);
        content.put("ElectricitySpent", electricitySpent);
        content.put("CookingGasSpent", cookingGasSpent);
        content.put("HeatingOilSpent", heatingOilSpent);
        content.put("PropaneSpent", propaneSpent);
        content.put("WoodenPalletSpent", woodenPalletSpent);
        content.put("LpgSpent", lpgSpent);
        content.put("TotalEmission", totalEmission);

        long result = DB.insert("HouseholdEmission", null, content);
        DB.close();
        return result != -1;
    }
    public Boolean insertFlightsEmission(String personalEmail, String source, String destination, int numberOfTrips,
                                         String classType, double totalEmission) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("PersonalEmail", personalEmail);
        content.put("Source", source);
        content.put("Destination", destination);
        content.put("NumberOfTrips", numberOfTrips);
        content.put("ClassType", classType);
        content.put("TotalEmission", totalEmission);

        long result = DB.insert("FlightsEmission", null, content);
        DB.close();
        return result != -1;
    }
    public Boolean insertPersonalCarEmission(String personalEmail, double mileage, String consumptionType,
                                             double consumption, String manufacturer, String model,
                                             double totalEmission) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("PersonalEmail", personalEmail);
        content.put("Mileage", mileage);
        content.put("ConsumptionType", consumptionType);
        content.put("Consumption", consumption);
        content.put("Manufacturer", manufacturer);
        content.put("Model", model);
        content.put("TotalEmission", totalEmission);

        long result = DB.insert("PersonalCarEmission", null, content);
        DB.close();
        return result != -1;
    }
    public Boolean insertBusRailEmission(String personalEmail, double busDistance, double coachDistance,
                                         double sgrDistance, double cabDistance, double totalEmission) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("PersonalEmail", personalEmail);
        content.put("BusDistance", busDistance);
        content.put("CoachDistance", coachDistance);
        content.put("SgrDistance", sgrDistance);
        content.put("CabDistance", cabDistance);
        content.put("TotalEmission", totalEmission);

        long result = DB.insert("BusRailEmission", null, content);
        DB.close();
        return result != -1;
    }

}
