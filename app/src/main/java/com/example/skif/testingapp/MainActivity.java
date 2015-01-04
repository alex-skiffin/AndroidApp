package com.example.skif.testingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private List<PhoneInfo> _allInfo;
    private ListView _listView;

    @Override
    protected void onStart(){
        super.onStart();
        getPrefs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        _listView = (ListView) findViewById(R.id.listView);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String info = _allInfo.get(position).PhoneName;
                String phoneId = _allInfo.get(position).Id.toString();
                Intent intent = new Intent(MainActivity.this, ShowPageCode.class);
                intent.putExtra("PhoneName", info);
                intent.putExtra("PhoneId", phoneId);
                startActivity(intent);
            }
        });
    }

    public void GetContacts() {
        try {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Cursor contacts = getContentResolver().query(uri, null, null, null, null);

            contacts.moveToFirst();
            AllInfo info = new AllInfo();
            do {
                AnonymousInfo contactInfo = new AnonymousInfo();
                contactInfo.ContactName = contacts.getString(25);
                for (int i = 0; i < contacts.getColumnCount(); i++) {
                    contactInfo.VerySecretInfo.put(contacts.getColumnName(i), contacts.getString(i));
                }

                info.AllInfos.add(contactInfo);
            } while (contacts.moveToNext());

            contacts.close();
            //_allInfo = info.AllInfos;
        } catch (Exception e) {
            String dd = e.getMessage();
        }
    }

    public void onClickToGetAll(View view) {
        /*GetContacts();
        if (_allInfo != null) {
            // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
            final ArrayAdapter<AnonymousInfo> adapter;
            adapter = new ArrayAdapter<AnonymousInfo>(this,
                    android.R.layout.simple_list_item_1, _allInfo);
            // Привяжем массив через адаптер к ListView
            _listView.setAdapter(adapter);
        }*/
        try {

            Requester ut = new Requester();
            ut.GetAll(Constants.ServerUrl+"/all_phones/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickToPost(View view) {
        Intent intent = new Intent(MainActivity.this, add_activity.class);
        startActivity(intent);
    }

    public void SetList(String response) {
        Gson gson = new Gson();
        try {
            AllPhone posts = gson.fromJson(response, AllPhone.class);
            _allInfo = posts.AllPhones;
            if (_allInfo != null) {
                // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
                final ArrayAdapter<PhoneInfo> adapter;
                adapter = new ArrayAdapter<PhoneInfo>(this,
                        android.R.layout.simple_list_item_1, _allInfo);
                // Привяжем массив через адаптер к ListView
                _listView.setAdapter(adapter);
            }
        } catch (Exception e) {
            String r = e.getMessage();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSettingsMenuClick(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        //addPreferencesFromResource(R.xml.preferences);
        //
    }

    private void getPrefs() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Constants.ServerUrl = prefs.getString("server_address", "http://192.168.1.105:7070");
    }
    class Requester {
        private String response;

        public AnonymousInfo Get(String id) throws IOException {
            String urlStr = Constants.ServerUrl+"/this/" + id;

            new RequestTask().execute(urlStr, urlStr, urlStr);
            //получаем ответ от сервера
            AnonymousInfo anInfo = new Gson().fromJson(response, AnonymousInfo.class);
            return anInfo;
        }

        public void GetAll(String url) throws IOException {
            new RequestTask().execute(url, url, url);
        }

        public String Post(String info) throws IOException {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost http = new HttpPost(Constants.ServerUrl+"/post");
            //AnonymousInfo anInfo = new AnonymousInfo();
            //anInfo.ContactName = info;
            Gson gson = new Gson();
            String anInfoJson = gson.toJson(_allInfo);

            String response = httpclient.execute(http, new BasicResponseHandler());
            return response;
        }

        class RequestTask extends AsyncTask<String, Void, String> {

            @Override
            public String doInBackground(String... uri) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse hResponse;
                String responseString = null;
                try {
                    HttpGet hGet = new HttpGet(uri[0]);
                    /*hGet.addHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like\n" +
                            "Gecko) Chrome/39.0.2171.71 Safari/537.36");*/
                    hResponse = httpclient.execute(hGet);
                    StatusLine statusLine = hResponse.getStatusLine();
                    if (statusLine.getStatusCode() == 200) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        hResponse.getEntity().writeTo(out);
                        out.close();
                        responseString = out.toString();
                    } else {
                        //Closes the connection.
                        hResponse.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (ClientProtocolException e) {
                    Log.d("nert", e.getMessage());
                } catch (IOException e) {
                    Log.d("nert", e.getMessage());
                }
                return responseString;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d("123", "Response: " + result);
                SetList(result);
            }

        }
    }
}
