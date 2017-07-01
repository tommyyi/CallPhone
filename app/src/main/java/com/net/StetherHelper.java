package com.net;

import android.content.Context;

import java.lang.reflect.Method;

import okhttp3.Interceptor;

/**
 * Created by 易剑锋 on 2017/4/27.
 * 通过反射初始化和得到拦截器，有助于适配release版本，因为stetho只会在debug版本引入
 * 【1】通过反射初始化stetho
 * 【2】通过反射得到stetho的okhttp3的拦截器
 */

public class StetherHelper
{
    /**
     * 通过反射得到stetho的okhttp3的拦截器
     * @return
     */
    public static Interceptor getStethoOkHttp3Interceptor()
    {
        try
        {
            Class<?> aClass = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor");
            Object instance = aClass.newInstance();
            return (Interceptor) instance;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射初始化stetho
     * @param context
     */
    public static void initStetho(Context context)
    {
        try
        {
            Class<?> loadClass = Class.forName("com.facebook.stetho.Stetho");
            Method method = loadClass.getDeclaredMethod("initializeWithDefaults", Context.class);
            method.invoke(null, context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
