package com.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 易剑锋 on 2017/7/1.
 */

public class PhoneBean
{

    /**
     * number : 13825258279
     */

    private String number;

    public PhoneBean(String number)
    {

    }

    public static List<PhoneBean> arrayListBeanFromData(String str)
    {

        Type listType = new TypeToken<ArrayList<PhoneBean>>()
        {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }
}
