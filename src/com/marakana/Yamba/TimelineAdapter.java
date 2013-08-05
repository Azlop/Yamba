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
 * Date: 8/5/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimelineAdapter extends SimpleCursorAdapter {

    static final String[] from = { DbHelper.C_CREATED_AT, DbHelper.C_USER, DbHelper.C_TEXT };
    static final int[] to = { R.id.textCreatedAt, R.id.textUser, R.id.textText };

    public TimelineAdapter(Context context, Cursor cursor) {
        super(context, R.layout.row, cursor, from, to);
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor){
        super.bindView(row, context, cursor);

        long timestamp = cursor.getLong(cursor.getColumnIndex(DbHelper.C_CREATED_AT));
        TextView textCreatedAt = (TextView) row.findViewById(R.id.textCreatedAt);
        textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp));

    }
}
