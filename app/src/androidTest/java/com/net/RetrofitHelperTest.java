package com.net;

import android.support.test.runner.AndroidJUnit4;

import com.Index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
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
        List<PhoneBean> phoneBeanList = NetHelper.getRemotePhoneBeanList(mContext, "https://raw.githubusercontent.com/tommyyi/CallPhone/master/phoneDir/phoneList.json");
        waiting();
    }

    @Test
    public void pullUrlList() throws Exception
    {
        List<TargetBean> urlList = NetHelper.getUrlList(mContext, Index.TARGET_JSON);
        waiting();
    }
}