package com.tool;

import android.content.Context;

import com.Index;
import com.net.PhoneBean;

import java.io.InputStream;
import java.util.List;

/**
 * Created by 易剑锋 on 2017/10/9.
 */

public class LocalHelper
{
    public static List<PhoneBean> getLocalPhoneBeanList(Context context)
    {
        try
        {
            String fileName = Index.index+".img";
            InputStream inputStream = context.getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String json = new String(buffer);
            return PhoneBean.arrayListBeanFromData(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
