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
    public static final String NEW_STATUS_INTENT = "com.marakana.yamba.NEW_STATUS";
    public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
    static final int DELAY = 60000; // a minute
    private static final String TAG = "UpdaterService";
    private boolean runflag = false;
    private Updater updater;
    private YambaApplication yamba;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!runflag) {
            this.runflag = true;
            this.updater.start();
            ((YambaApplication) super.getApplication()).setServiceRunning(true);

            Log.d(TAG, "onStarted");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runflag = false;
        this.updater.interrupt();
        this.updater = null;
        ((YambaApplication) super.getApplication()).setServiceRunning(false);

        Log.d(TAG, "onDestroyed");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.updater = new Updater();

        Log.d(TAG, "onCreated");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Thread that performs the actual update from online service
    private class Updater extends Thread {

        public Updater() {
            super("UpdaterService-Updater");
        }

        @Override
        public void run() {
            UpdaterService updaterService = UpdaterService.this;
            while (updaterService.runflag) {
                Log.d(TAG, "Running background thread");
                try {
                    YambaApplication yamba = (YambaApplication) updaterService.getApplication();
                    int newUpdates = yamba.fetchStatusUpdates();
                    if (newUpdates > 0) {
                        Log.d(TAG, "We have a new status");
                    }
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    updaterService.runflag = false;
                }
            }
        }
    }
}
