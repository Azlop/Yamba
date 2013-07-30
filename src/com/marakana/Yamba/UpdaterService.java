package com.marakana.Yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.runflag = true;
        this.updater.start();
        Log.d(TAG, "onStarted");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runflag = false;
        this.updater.interrupt();
        this.updater = null;
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
    private class Updater extends Thread{
        public Updater(){
            super("UpdaterService-Updater");
        }

        @Override
        public void run() {
            UpdaterService updaterService = UpdaterService.this;

            while(updaterService.runflag){
                Log.d(TAG, "Updater running...");
                try {
                    Log.d(TAG, "Updater ran");
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    updaterService.runflag = false;
                }
            }
        }
    }
}
