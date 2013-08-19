package com.marakana.Yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/9/13
 * Time: 3:20 PM
 *
 * Description: Class that starts automatically the UpdaterService when boot is completed.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, UpdaterService.class));
        Log.d("BootReceiver", "onReceived");
    }
}
