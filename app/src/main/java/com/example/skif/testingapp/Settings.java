package com.example.skif.testingapp;


import android.app.Activity;
import android.os.Bundle;

public class Settings extends Activity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.sett_layout);
    }
}
