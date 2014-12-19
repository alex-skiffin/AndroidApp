package com.example.skif.testingapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;

public class add_activity extends Activity {
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.add_page);
    }

    public void onClick2Back(View view) {
        finish();
    }

    public void SendToServer(View view) throws IOException, JSONException {
        TextView texts = (TextView) findViewById(R.id.textView2);
        new RequestPostTask().execute(texts.getText().toString(), "", "");
        finish();
    }

    class RequestPostTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... uri) {
            String result = "Not Good";
            try {
                InputStream inputStream = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost(Constants.ServerUrl+"phone/0");
                PhoneInfo anInfo = new PhoneInfo();
                anInfo.PhoneName = uri[0].toString();
                Gson gson = new Gson();
                String anInfoJson = gson.toJson(anInfo);
                StringEntity se = new StringEntity(anInfoJson);
                http.setEntity(se);
                http.setHeader("Accept", "application/json");
                http.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(http);
                inputStream = httpResponse.getEntity().getContent();
                result = inputStream.toString();
            } catch (Exception exeption) {
                result = exeption.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("123", "Response: " + result);
        }
    }
}
