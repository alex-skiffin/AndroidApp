package com.example.skif.testingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowContactActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_contact);
        TextView tName = (TextView) findViewById(R.id.ContactName);
        TextView tNumber = (TextView) findViewById(R.id.PhoneNumber);
        String Name = getIntent().getExtras().getString("Name");
        String Number = getIntent().getExtras().getString("Number");
        tName.setText(Name);
        tNumber.setText(Number);
    }
    public void Close(View view)
    {
        finish();
    }
}
