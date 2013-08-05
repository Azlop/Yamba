package com.marakana.Yamba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter.ViewBinder;


/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/5/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimelineActivity extends BaseActivity {

    Cursor cursor;
    ListView listTimeline;
    SimpleCursorAdapter adapter;
    static final String[] FROM = { DbHelper.C_CREATED_AT, DbHelper.C_USER, DbHelper.C_TEXT };
    static final int[] TO = { R.id.textCreatedAt, R.id.textUser, R.id.textText };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        // Check if preferences have been set
        if (yambaApplication.getPrefs().getString("username", null) == null) { // <2>
            startActivity(new Intent(this, PrefsActivity.class));
            Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG).show();
        }

        listTimeline = (ListView) findViewById(R.id.listTimeline);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        yambaApplication.getStatusData().close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup list
        this.setupList();
    }

    // Responsible for fetching data and setting up the list and the adapter
    private void setupList() {
        // Get the data
        cursor = yambaApplication.getStatusData().getStatusUpdates();
        startManagingCursor(cursor);

        // Setup Adapter
        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
        adapter.setViewBinder(VIEW_BINDER);
        listTimeline.setAdapter(adapter);
    }

    // View binder constant to inject business logic for timestamp to relative
    // time conversion
    static final ViewBinder VIEW_BINDER = new ViewBinder() {

        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() != R.id.textCreatedAt)
                return false;

            // Update the created at text to relative time
            long timestamp = cursor.getLong(columnIndex);
            CharSequence relTime = DateUtils.getRelativeTimeSpanString(view
                    .getContext(), timestamp);
            ((TextView) view).setText(relTime);

            return true;
        }
    };
}
