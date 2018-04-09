package com.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 1个TargetBean存储1个url，这个url存储1个用户需要的编码列表
 */

public class TargetBean
{
    /**
     * url : https://raw.githubusercontent.com/teamo20150201/IO/master/ANGLE/0.json
     */

    private String url;

    /**
     * @param str json数组，每个元素是1个TargetBean的json
     * @return
     */
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
