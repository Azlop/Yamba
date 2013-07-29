package com.marakana.Yamba;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class StatusActivity extends Activity implements OnClickListener, TextWatcher
{
    private static final String TAG = "StatusActivity";
    EditText editText;
    Button updateButton;
    Twitter twitter;
    TextView textCount;

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

    // Asynchronously posts to Twitter
    class PostToTwitter extends AsyncTask<String, Integer, String>{
        // Initiate background activity
        @Override
        protected String doInBackground(String... params) {
            try{
                winterwell.jtwitter.Status status = twitter.updateStatus(params[0]);
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
        String status = editText.getText().toString();
        new PostToTwitter().execute(status);
        Log.d(TAG, "onClicked");
    }

    // When a options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
}
