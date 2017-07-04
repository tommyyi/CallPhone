package com.monitor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog;
import android.util.Log;

import com.LogDeleted;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 易剑锋 on 2017/7/3.
 */

public class CallLogHandler
{
    private Handler mHandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message msg)
        {
            String msgBody = (String)msg.obj;
            EventBus.getDefault().post(new LogDeleted(msgBody));
            Log.i("Log", msg.obj + ":" + msgBody);
        }
    };
    private CallLogObserver mCallLogObserver;

    public void register(Context context)
    {
        mCallLogObserver = new CallLogObserver(mHandler, context);
        context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, mCallLogObserver);//equivalent to【Uri.parse("content://call_log/calls")】
    }

    public void unRegister(Context context)
    {
        context.getContentResolver().unregisterContentObserver(mCallLogObserver);//equivalent to【Uri.parse("content://call_log/calls")】
    }
}
