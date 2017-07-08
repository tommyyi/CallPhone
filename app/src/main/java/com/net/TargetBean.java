package com.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 易剑锋 on 2017/7/9.
 */

public class TargetBean
{
    /**
     * url : https://raw.githubusercontent.com/teamo20150201/IO/master/ANGLE/0.json
     */

    private String url;

    public static List<TargetBean> arrayTargetBeanFromData(String str)
    {
        Type listType = new TypeToken<ArrayList<TargetBean>>()
        {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
