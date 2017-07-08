package com.net;

import android.support.test.runner.AndroidJUnit4;

import com.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by 易剑锋 on 2017/7/1.
 */
@RunWith(AndroidJUnit4.class)
public class RetrofitHelperTest extends TestBase
{
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void pullList() throws Exception
    {
        List<PhoneBean> phoneBeanList = NetHelper.refresh(mContext, "https://raw.githubusercontent.com/tommyyi/CallPhone/master/phoneDir/phoneList.json");
        waiting();
    }

    @Test
    public void pullUrlList() throws Exception
    {
        List<TargetBean> urlList = NetHelper.getUrlList(mContext, MainActivity.TARGET_JSON);
        waiting();
    }
}