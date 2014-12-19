package com.example.skif.testingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by skif on 14.12.2014.
 */
public class ShowPageCode extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_page);

        String info = "";
        info = getIntent().getExtras().getString("info");
        TextView infoTextView = (TextView)findViewById(R.id.showText);
        infoTextView.setText(info);
    }
    public void goToBack(View view){finish();}
}