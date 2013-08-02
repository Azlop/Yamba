package com.marakana.Yamba;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import winterwell.jtwitter.Twitter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 7/30/13
 * Time: 11:49 AM
 *
 * Description:
 */
public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = YambaApplication.class.getName();
    public Twitter twitter;
    private SharedPreferences prefs;
    private boolean serviceRunning;
    private StatusData statusData;

    public StatusData getStatusData(){
        return statusData;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.twitter = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs.registerOnSharedPreferenceChangeListener(this);
        Log.i(TAG, "onCreated");
    }

    // Cleanup when app is shut down
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminated");
    }

    // Only one thread is allowed to go inside this method
    public synchronized Twitter getTwitter(){
        if(this.twitter == null){
            String username = this.prefs.getString("username", "");
            String password = this.prefs.getString("password", "");
            String apiRoot = this.prefs.getString("apiRoot", "http://yamba.marakana.com/api");

            if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(apiRoot)){
                this.twitter = new Twitter(username, password);
                this.twitter.setAPIRootUrl(apiRoot);
            }
        }
        return this.twitter;
    }

    // Connects to online service and puts the latest statuses into DB.
    // Returns the count of statuses
    public synchronized int fetchStatusUpdates(){
        Log.d(TAG, "Fetching status updates");
        Twitter twitter1 = this.getTwitter();

        if(twitter1 == null){
            Log.d(TAG, "Twitter connection info not initialized");
            return 0;
        }

        try {
            List<winterwell.jtwitter.Status> statusUpdates = twitter.getHomeTimeline();
            long latestStatusCreatedAtTime = this.getStatusData().getLatestStatusCreatedAtTime();
            int count = 0;
            ContentValues values = new ContentValues();

            for (winterwell.jtwitter.Status status : statusUpdates){
                values.put(StatusData.C_ID, status.id.toString());
                long createdAt = status.getCreatedAt().getTime();
                values.put(StatusData.C_CREATED_AT, createdAt);
                values.put(StatusData.C_TEXT, status.text);
                values.put(StatusData.C_USER, status.user.name);

                Log.d(TAG, "Got update with id "+status.getId()+". Saving");
                this.getStatusData().insertOrIgnore(values);

                if(latestStatusCreatedAtTime < createdAt){
                    count++;
                }
            }

            Log.d(TAG, count > 0 ? "Got "+count+" status updates" : "No new status updates");
            return count;
        } catch (RuntimeException e) {
            Log.e(TAG, "Failed to fetch status updates", e);
            return 0;
        }
    }
}
