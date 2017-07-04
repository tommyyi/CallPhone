package com.net;

import android.support.test.runner.AndroidJUnit4;

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
    public void pullLIST() throws Exception
    {
        List<PhoneBean> phoneBeanList = NetHelper.refresh(mContext, "phoneList.json");
    }
}