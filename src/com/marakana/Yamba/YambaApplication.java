package com.marakana.Yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import winterwell.jtwitter.Twitter;

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
}
