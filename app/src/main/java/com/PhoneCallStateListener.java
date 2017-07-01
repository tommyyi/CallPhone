package com;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class PhoneCallStateListener extends PhoneStateListener
{
    private static final String TAG = "状态";

    @Override
    public void onCallStateChanged(int state, String incomingNumber)
    {
        super.onCallStateChanged(state, incomingNumber);
        switch (state)
        {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(TAG, "CALL_STATE_IDLE");
                EventBus.getDefault().post(new EVENT_CALL_NEXT());
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(TAG, "CALL_STATE_OFFHOOK");

                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(TAG, "CALL_STATE_RINGING");

                break;
            default:
                Log.i(TAG, "state");
                break;
        }
    }
}
