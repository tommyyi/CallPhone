package com;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private int mIndex=0;
    private List<PhoneBean> mPhoneBeanList=new ArrayList<>();
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

        mButton = (Button)findViewById(R.id.btn_call_phone);
        mButton.setEnabled(false);
        etPhone = (EditText) findViewById(R.id.et_phone_num);
        EventBus.getDefault().register(this);

        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                List<TargetBean> urlList = NetHelper.getUrlList(getApplicationContext(), Index.TARGET_JSON);
                if(urlList==null||urlList.size()==0)
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
                    return;
                }

                String url = urlList.get(Index.index).getUrl();
                List<PhoneBean> phoneBeanList = NetHelper.refresh(getApplicationContext(), url);

                /*没有获取到号码*/
                if(phoneBeanList==null||phoneBeanList.size()==0)
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
                    return;
                }

                boolean needPost = true;
                if(phoneBeanList.size() != 0)
                {
                    String latest = PreferenceUtil.getString(getApplicationContext(), LATEST);
                    for(int index=0;index<phoneBeanList.size();index++)
                    {
                        if(phoneBeanList.get(index).getNumber().equals(latest))
                        {
                            if (index==phoneBeanList.size()-1)
                            {
                                // 最后一个拨打的号码是最后一个号码，已经打完了，不要循环拨打
                                needPost = false;
                                //mIndex=0;
                            }
                            else
                            {
                                mIndex=index+1;
                            }
                            break;
                        }
                    }
                }

                if (needPost)
                {
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
                            /*这个时候，将无法点击拨号按钮*/
                            allCallsAreDone();
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    private void allCallsAreDone()
    {
        mButton.setEnabled(false);
        mButton.setText("需要更新表，更新后退出本软件并重新启动本软件");
    }

    public void onCall(View view)
    {
        String nextNum = getNumber();
        if(nextNum!=null)
            dial(nextNum);
    }

    /**
     * @return 从列表中获取要拨打的number
     */
    private String getNumber()
    {
        if(mPhoneBeanList.size()==0)
        {
            Toast.makeText(MainActivity.this, "加载中", Toast.LENGTH_SHORT).show();
            return null;
        }

        if(mIndex>=mPhoneBeanList.size())
        {
            /*号码已经拨打完毕，让用户无法继续点击拨打按钮*/
            allCallsAreDone();
            return null;
        }

        String number = mPhoneBeanList.get(mIndex).getNumber();
        /*指向下一个要拨打的number*/
        mIndex++;
        return number;
    }

    private void dial(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.replace(" ","")));
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this, "没有授予拨打电话权限", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        PreferenceUtil.putString(getApplicationContext(), LATEST,number.replace(" ",""));
    }

    /**
     * @param eventCallNext 指示可以呼叫下一个了
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
        if (phoneBeanList!=null&&phoneBeanList.size()!=0)
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
        TextView view = (TextView)findViewById(R.id.tv_log);
        CharSequence text = view.getText();
        view.setText(text+"\r\n"+ eventLogDeleted.getMsgBody());
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
            Toast.makeText(MainActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPhoneBeanList.add(new PhoneBean(number));
        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
    }
}