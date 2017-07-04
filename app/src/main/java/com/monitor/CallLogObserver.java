package com.monitor;

/**
 * Created by 易剑锋 on 2017/7/3.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;

public class CallLogObserver extends ContentObserver
{
    /**query for latest communication*/
    public static final int MSG_CALL_LOG_QUERY_WHAT = 4;
    private Handler mHandler;
    private Uri uri = CallLog.Calls.CONTENT_URI;//equivalent to【Uri.parse("content://call_log/calls")】
    private ContentResolver resolver;
    
    public CallLogObserver(Handler handler, Context context)
    {
        super(handler);
        this.mHandler = handler;
        resolver = context.getContentResolver();
    }
    
    @Override
    public void onChange(boolean selfChange)
    {
        Cursor cursor = resolver.query(uri, null, null, null, "_id desc limit 3");
        int count=0;
        if (cursor != null&&cursor.moveToFirst())
        {
            boolean flag = cursor.moveToNext();
            flag = cursor.moveToNext();
            while (flag)
            {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));//1 .INCOMING_TYPE；2  .OUTGOING_；3  .MISSED_
                String number = cursor.getString(cursor.getColumnIndex("number"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));//calling time
                String formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(date));
                int duration = cursor.getInt(cursor.getColumnIndex("duration"));//calling duration
                if (duration<=3)
                {
                    int num = resolver.delete(uri, "_id=?", new String[] {_id + ""});
                    mHandler.sendMessage(Message.obtain(mHandler, MSG_CALL_LOG_QUERY_WHAT, number+"call record deleted"));
                }
                count++;
                if(count>3)
                    break;
                flag=cursor.moveToNext();
            }
            cursor.close();
        }
    }
}
