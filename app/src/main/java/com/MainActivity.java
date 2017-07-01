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

import com.net.NetHelper;
import com.net.PhoneBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private EditText etPhone;
    private PhoneCallStateListener mPhoneCallStateListener;
    private int mIndex=0;
    private List<PhoneBean> mPhoneBeanList=new ArrayList<>();

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

        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                List<PhoneBean> phoneBeanList = NetHelper.refresh(getApplicationContext(), "phoneList.json");
                EventBus.getDefault().post(phoneBeanList);
            }
        };
        new Thread(runnable).start();
    }

    public void onCall(View view)
    {
        String nextNum = getNextNum();
        if(nextNum!=null)
            dial(nextNum);
    }

    private String getNextNum()
    {
        if(mPhoneBeanList.size()==0)
        {
            Toast.makeText(MainActivity.this, "您还没有添加号码", Toast.LENGTH_SHORT).show();
            return null;
        }
        String number = mPhoneBeanList.get(mIndex).getNumber();
        mIndex++;
        if(mIndex==mPhoneBeanList.size())
        {
            //从头开始
            mIndex=0;
        }
        return number;
    }

    private void dial(String number)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(List<PhoneBean> phoneBeanList)
    {
        if (phoneBeanList!=null&&phoneBeanList.size()!=0)
        {
            mPhoneBeanList = phoneBeanList;
        }
        Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneCallStateListener, PhoneStateListener.LISTEN_NONE);

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onAddNumber(View view)
    {
        String number = etPhone.getText().toString();
        if (number.equals(""))
        {
            Toast.makeText(MainActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPhoneBeanList.add(new PhoneBean(number));
        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
    }
}