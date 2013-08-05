package com.marakana.Yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/5/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {
    YambaApplication yambaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yambaApplication = (YambaApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPrefs:
                startActivity(new Intent(this, PrefsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemToggleService:
                if (yambaApplication.isServiceRunning()) {
                    stopService(new Intent(this, UpdaterService.class));
                } else {
                    startService(new Intent(this, UpdaterService.class));
                }
                break;
            case R.id.itemPurge:
                ((YambaApplication) getApplication()).getStatusData().delete();
                Toast.makeText(this, R.string.msgAllDataPurged, Toast.LENGTH_LONG).show();
                break;
            case R.id.itemTimeline:
                startActivity(new Intent(this, TimelineActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemStatus:
                startActivity(new Intent(this, StatusActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
        if(yambaApplication.isServiceRunning()){
            toggleItem.setTitle(R.string.titleServiceStop);
            toggleItem.setIcon(android.R.drawable.ic_media_pause);
        } else {
            toggleItem.setTitle(R.string.titleServiceStart);
            toggleItem.setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }
}
