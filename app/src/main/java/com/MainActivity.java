package com;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity
{
    private EditText etPhone;
    private PhoneCallStateListener mPhoneCallStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        mPhoneCallStateListener = new PhoneCallStateListener();
        tm.listen(mPhoneCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        etPhone = (EditText) findViewById(R.id.et_phone_num);
        EventBus.getDefault().register(this);
    }

    public void onCall(View view)
    {
        String number = etPhone.getText().toString();
        if (number.equals(""))
        {
            Toast.makeText(MainActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        call(number);
    }

    private void call(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this, "没有授予拨打电话权限", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallNext(EVENT_CALL_NEXT next)
    {
        onCall(null);
    }

    @Override
    protected void onDestroy()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneCallStateListener, PhoneStateListener.LISTEN_NONE);

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}