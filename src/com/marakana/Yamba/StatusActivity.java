package com.marakana.Yamba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "StatusActivity";
    EditText editText;
    Button updateButton;
    Twitter twitter;
    TextView textCount;
    SharedPreferences prefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        // Find Views
        editText = (EditText) findViewById(R.id.editText);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(this);
        textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editText.addTextChangedListener(this);

        twitter = new Twitter("student","password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");

        // Setup Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 140 - s.length();
        textCount.setText(Integer.toString(count));
        textCount.setTextColor(Color.GREEN);
        if(count < 10){
            textCount.setTextColor(Color.YELLOW);
        }
        if(count < 0){
            textCount.setTextColor(Color.RED);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // invalidate twitter object
        twitter = null;
    }

    // Asynchronously posts to Twitter
    class PostToTwitter extends AsyncTask<String, Integer, String>{
        // Initiate background activity
        @Override
        protected String doInBackground(String... params) {
            try{
                // Use getTwitter method from YambaApplication.java
                YambaApplication yamba = (YambaApplication) getApplication();
                winterwell.jtwitter.Status status = yamba.getTwitter().updateStatus(params[0]);
                return status.text;
            } catch (TwitterException e){
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return "Failed to post";
            }
        }

        // When activity is completed
        @Override
        protected void onPostExecute(String s) {
            // Show message when task is completed
            Toast.makeText(StatusActivity.this, s, Toast.LENGTH_LONG).show();
        }

        // When there's a status to be updated
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    @Override
    public void onClick(View v) {
        // Update twitter status
        try{
            getTwitter().setStatus(editText.getText().toString());
        }
        catch (TwitterException e){
            Log.d(TAG, "Twitter setStatus failed: "+e);
        }
    }

    // When a options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemServiceStart:
                startService(new Intent(this, UpdaterService.class));
                break;
            case R.id.itemServiceStop:
                stopService(new Intent(this, UpdaterService.class));
                break;
            case R.id.itemPrefs:
                startActivity(new Intent(this, PrefsActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private Twitter getTwitter(){
        if(twitter == null){
            String username,password,apiRoot;
            username = prefs.getString("username", "");
            password = prefs.getString("password", "");
            apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");

            // Connect to twitter.com
            twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(apiRoot);
        }
        return twitter;
    }
}
