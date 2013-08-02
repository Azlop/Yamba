package com.marakana.Yamba;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 7/30/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdaterService extends Service {
    private static final String TAG = "UpdaterService";
    static final int DELAY = 60000; // a minute
    private boolean runflag = false;
    private Updater updater;
    private YambaApplication yamba;
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.runflag = true;
        this.updater.start();
        this.yamba.setServiceRunning(true);
        Log.d(TAG, "onStarted");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runflag = false;
        this.updater.interrupt();
        this.updater = null;
        this.yamba.setServiceRunning(false);
        Log.d(TAG, "onDestroyed");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.yamba = (YambaApplication) getApplication();
        this.updater = new Updater();
        dbHelper = new DbHelper(this);
        Log.d(TAG, "onCreated");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Thread that performs the actual update from online service
    private class Updater extends Thread{
        List<winterwell.jtwitter.Status> timeline;

        public Updater(){
            super("UpdaterService-Updater");
        }

        @Override
        public void run() {
            UpdaterService updaterService = UpdaterService.this;

            while(updaterService.runflag){
                Log.d(TAG, "Updater running...");
                try {
                    // Get the timeline from the cloud
                    try {
                        timeline = yamba.getTwitter().getHomeTimeline();
                    } catch (TwitterException e){
                        Log.e(TAG, "Failed to connect to twitter service", e);
                    }

                    // open database for writing
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    // Loop over the timeline and print it out
                    for(winterwell.jtwitter.Status status : timeline){
                        // insert into database
                        values.clear();
                        values.put(DbHelper.C_ID, status.id.toString());
                        values.put(DbHelper.C_CREATED_AT, status.getCreatedAt().getTime());
                        values.put(DbHelper.C_SOURCE, status.source);
                        values.put(DbHelper.C_TEXT, status.text);
                        values.put(DbHelper.C_USER, status.user.name);
                        db.insertOrThrow(DbHelper.TABLE, null, values);

                        Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
                    }

                    // close database
                    db.close();

                    Log.d(TAG, "Updater ran");
                    Thread.sleep(DELAY);

                } catch (InterruptedException e) {
                    updaterService.runflag = false;
                }
            }
        }
    }
}
