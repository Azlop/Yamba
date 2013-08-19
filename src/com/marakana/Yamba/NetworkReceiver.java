package com.marakana.Yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/9/13
 * Time: 3:56 PM
 *
 * Description: Class that checks network connectivity
 */
public class NetworkReceiver extends BroadcastReceiver{
    static final String TAG = "NetworkReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if (isNetworkDown){
            Log.d(TAG, "OnReceive: NOT connected, stopping UpdaterService");
            context.stopService(new Intent(context, UpdaterService.class));
        } else {
            Log.d(TAG, "OnReceive: connected, starting UpdaterService");
            context.startService(new Intent(context, UpdaterService.class));
        }
    }
}
