package com.marakana.Yamba;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/3/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimelineAdapter extends SimpleCursorAdapter {

    static final String[] FROM = {DbHelper.C_CREATED_AT, DbHelper.C_USER, DbHelper.C_TEXT};
    static final int[] TO = {R.id.textCreatedAt, R.id.textUser, R.id.textText};

    public TimelineAdapter(Context context, Cursor c){
        super(context, R.layout.row, c, FROM, TO);
    }

    // This is where the actual binding of a cursor to view happens
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        // Manually bind created timestamp to its view
        long timestamp = cursor.getLong(cursor.getColumnIndex(DbHelper.C_CREATED_AT));
        TextView textCreatedAt = (TextView) view.findViewById(R.id.textCreatedAt);
        textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp));
    }
}
