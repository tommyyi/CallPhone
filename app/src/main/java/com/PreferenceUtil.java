/*      
 * 描述: SharePreferences工具类             
 */
package com;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * SharePreferences操作工具类
 */
public class PreferenceUtil
{

    public static final String SHARE_NAME = "NewsToday";

    /**
     * 从缓存文件中获取整形数据
     * 修改人: [ulegendtimes][tangdongwei]
     * @param context
     * @param key
     * @param defaultValue
     * @return 没有该关键字时为0
     * @see [类、类#方法、类#成员]
     * 修改日期: 2015-9-10 上午11:32:31
     */
    public static int getInt(Context context, String key, int defaultValue)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        int data = mPreferences.getInt(key, defaultValue);
        return data;
    }

    public static int getInt(Context context, String shareFileName, String key)
    {
        if (TextUtils.isEmpty(shareFileName) || TextUtils.isEmpty(key))
            return -1;
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        int data = mPreferences.getInt(key, 0);
        return data;
    }

    public static int getInt(Context context, String shareFileName, String key, int defaultValue)
    {
        if (TextUtils.isEmpty(shareFileName) || TextUtils.isEmpty(key))
            return -1;
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        int data = mPreferences.getInt(key, defaultValue);
        return data;
    }

    /**
     * 保存整形数据到缓存文件中
     * 修改人: [ulegendtimes][tangdongwei]
     * @param context
     * @param key
     * @param value
     * @see [类、类#方法、类#成员]
     * 修改日期: 2015-9-10 上午11:33:18
     */
    public static void putInt(Context context, String key, int value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putInt(Context context, String shareFileName, String key, int value)
    {
        if (TextUtils.isEmpty(shareFileName) || TextUtils.isEmpty(key))
            return;
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    /**
     * 获取key值
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key)
    {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        String data = mPreferences.getString(key, defaultValue);
        return data;
    }

    public static String getString(Context context, String shareFileName, String key, String defaultValue)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        String data = mPreferences.getString(key, defaultValue);
        return data;
    }


    /**
     * 获取key值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue)
    {
        if (context == null)
            return false;

        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        Boolean data = mPreferences.getBoolean(key, defaultValue);
        return data;
    }

    public static boolean getBoolean(Context context, String shareFileName, String key, boolean defaultValue)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        Boolean data = mPreferences.getBoolean(key, defaultValue);
        return data;
    }

    /**
     * 获取key值
     * @param context
     * @param key
     * @param defaultData
     * @return
     */
    public static long getLong(Context context, String key, long defaultData)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        Long data = mPreferences.getLong(key, defaultData);
        return data;
    }

    public static long getLong(Context context, String shareFileName, String key)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        Long data = mPreferences.getLong(key, 0);
        return data;
    }

    public static long getLong(Context context, String shareFileName, String key, long defaultData)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        Long data = mPreferences.getLong(key, defaultData);
        return data;
    }

    /**
     * SharePerferences插入String
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putString(Context context, String shareFileName, String key, String value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * SharePerferences插入boolean
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value)
    {
        if (context == null)
            return;

        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putBoolean(Context context, String shareFileName, String key, boolean value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * SharePerferences插入Long
     * @param context
     * @param key
     * @param value
     */
    public static void putLong(Context context, String key, long value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putLong(Context context, String shareFileName, String key, long value)
    {
        SharedPreferences mPreferences = context.getSharedPreferences(shareFileName, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void clear(Context context, String shareFileName)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
