package com.net;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/*测试基类
【1】启动activity
【2】获取mContext
【3】提供等待函数*/

@RunWith(AndroidJUnit4.class)
public class TestBase
{
    public Context mContext;

    public void setUp() throws Exception
    {
        mContext = InstrumentationRegistry.getTargetContext();
        StetherHelper.initStetho(mContext);
    }

    public void waiting() throws Exception
    {
        Thread.sleep(1000000000);
    }
}
