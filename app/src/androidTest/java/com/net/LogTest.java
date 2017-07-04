package com.net;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.laser.message.TestBase;
import com.monitor.CallLogHandler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Example local unit test, which will doStep1 on the development machine (host).
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LogTest extends TestBase
{
    @Override
    @Before
    public void setUp()
        throws Exception
    {
        super.setUp();
    }

    @Test
    public void test201_67() throws Exception
    {
        CallLogHandler callLogHandler = new CallLogHandler();
        callLogHandler.register(mContext);
        if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
        {
            dial();
        }
        waiting();
    }

    private void dial()
    {
        String phone="13825258279";
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}