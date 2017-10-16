package com;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.monitor.CallLogHandler;
import com.net.NetHelper;
import com.net.PhoneBean;
import com.net.TargetBean;
import com.tool.LocalHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private static final String LATEST = "latest";
    private EditText etPhone;
    private PhoneCallStateListener mPhoneCallStateListener;
    private int mIndex = 0;
    private List<PhoneBean> mPhoneBeanList = new ArrayList<>();
    private CallLogHandler mCallLogHandler;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCallLogHandler = new CallLogHandler();
        mCallLogHandler.register(getApplicationContext());

        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        mPhoneCallStateListener = new PhoneCallStateListener();
        tm.listen(mPhoneCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        mButton = (Button) findViewById(R.id.btn_call_phone);
        mButton.setEnabled(false);
        etPhone = (EditText) findViewById(R.id.et_phone_num);
        EventBus.getDefault().register(this);

        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                List<TargetBean> urlList;
                List<PhoneBean> phoneBeanList;

                /*获取company的配置文件*/
                urlList = NetHelper.getUrlList(getApplicationContext(), Index.TARGET_JSON);
                if (urlList == null || urlList.size() == 0)
                {
                    handleLocalPhoneBeanList();
                }
                else
                {
                    /*从company的配置文件中，获取company的某个用户对应的url*/
                    String url = urlList.get(Index.index).getUrl();
                    /*通过该url，获取该用户需要的编码列表*/
                    phoneBeanList = NetHelper.getRemotePhoneBeanList(getApplicationContext(), url);

                    /*没有获取到编码列表：提示该情况，返回*/
                    if (phoneBeanList == null || phoneBeanList.size() == 0)
                    {
                        handleLocalPhoneBeanList();
                    }
                    else
                    {
                        phoneBeanList = getUnrepeatedList(phoneBeanList);
                        loadPhoneBeanListSuccess(phoneBeanList);
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private List<PhoneBean> getUnrepeatedList(List<PhoneBean> originalList)
    {
        if(originalList == null || originalList.size() == 0)
            return new ArrayList<>();

        List<PhoneBean> currentList = new ArrayList<>();
        int size = originalList.size();
        for(int index = 0; index < size; index++)
        {
            if(!isExist(originalList.get(index), currentList))
                currentList.add(originalList.get(index));
        }
        return currentList;
    }

    private boolean isExist(PhoneBean phoneBean, List<PhoneBean> currentList)
    {
        if(currentList == null || currentList.size() ==0)
            return false;

        int size = currentList.size();
        for (int index=0;index<size;index++)
        {
            if(phoneBean.getNumber().equals(currentList.get(index).getNumber()))
                return true;
        }
        return false;
    }

    private void handleLocalPhoneBeanList()
    {
        List<PhoneBean> phoneBeanList = LocalHelper.getLocalPhoneBeanList(getApplicationContext());
        if (phoneBeanList == null || phoneBeanList.size() == 0)
        {
            loadPhoneBeanListFail();
        }
        else
        {
            phoneBeanList = getUnrepeatedList(phoneBeanList);
            loadPhoneBeanListSuccess(phoneBeanList);
        }
    }

    private void loadPhoneBeanListFail()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                netFail();
            }
        });
    }

    private void loadPhoneBeanListSuccess(List<PhoneBean> phoneBeanList)
    {
        boolean canContinue = true;
        if (phoneBeanList !=null && phoneBeanList.size() != 0)
        {
            String latest = PreferenceUtil.getString(getApplicationContext(), LATEST);
            for (int index = 0; index < phoneBeanList.size(); index++)
            {
                if (phoneBeanList.get(index).getNumber().equals(latest))
                {
                    if (index == phoneBeanList.size() - 1)
                    {
                        // 最后一个使用的编码是列表的最后一个编码，已经干完了，不能继续干了
                        canContinue = false;
                        //mIndex=0;
                    }
                    else
                    {
                        mIndex = index + 1;
                    }
                    break;
                }
            }
        }
        else
        {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    /*提示编码已经使用完毕，不能继续干了*/
                    allCallsAreDone();
                }
            });
            return;
        }

        if (canContinue)
        {
            /*提示可以继续干*/
            EventBus.getDefault().post(phoneBeanList);
        }
        else
        {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    /*提示编码已经使用完毕，不能继续干了*/
                    allCallsAreDone();
                }
            });
        }
    }

    private void allCallsAreDone()
    {
        mButton.setEnabled(false);
        mButton.setText("需要更新列表，更新后退出本软件并重新启动本软件");
    }

    public void onCall(View view)
    {
        String nextNum = getNumber();
        if (nextNum != null)
            dial(nextNum);
    }

    /**
     * @return 从列表中获取要使用的编码
     */
    private String getNumber()
    {
        if (mPhoneBeanList.size() == 0)
        {
            Toast.makeText(MainActivity.this, "加载中", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mIndex >= mPhoneBeanList.size())
        {
            /*编码已经使用完*/
            allCallsAreDone();
            return null;
        }

        /*获取本次操作的编码*/
        String number = mPhoneBeanList.get(mIndex).getNumber();
        /*指向下一个要操作的编码*/
        mIndex++;
        return number;
    }

    private void dial(String number)
    {
        String exactNumber = number.replace(" ", "");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + exactNumber));
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this, "没有授予拨打电话权限", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        /*记录本次操作的编码*/
        PreferenceUtil.putString(getApplicationContext(), LATEST, exactNumber);
    }

    /**
     * @param eventCallNext 指示操作下一个编码
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallNext(EventCallNext eventCallNext)
    {
        View view = null;
        onCall(view);
    }

    /**
     * @param phoneBeanList 从remote加载的列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoad(List<PhoneBean> phoneBeanList)
    {
        if (phoneBeanList != null && phoneBeanList.size() != 0)
        {
            mPhoneBeanList = phoneBeanList;
            mButton.setText("点击拨打");
            mButton.setEnabled(true);
        }
        else
        {
            netFail();
        }
    }

    private void netFail()
    {
        mButton.setText("加载失败，退出重试");
        mButton.setEnabled(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleted(EventLogDeleted eventLogDeleted)
    {
        TextView view = (TextView) findViewById(R.id.tv_log);
        CharSequence text = view.getText();
        view.setText(text + "\r\n" + eventLogDeleted.getMsgBody());
    }

    @Override
    protected void onDestroy()
    {
        mCallLogHandler.unRegister(getApplicationContext());
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
            Toast.makeText(MainActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPhoneBeanList.add(new PhoneBean(number));
        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
    }
}