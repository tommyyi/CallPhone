package com.net;

import android.content.Context;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 易剑锋 on 2017/7/1.
 */

public class NetHelper
{
    public static List<PhoneBean> refresh(Context context, final String fileName)
    {
        try
        {
            RetrofitHelper.INetworkPort iNetworkPort = RetrofitHelper.init(context);
            Call<ResponseBody> call = iNetworkPort.getList("https://raw.githubusercontent.com/tommyyi/CallPhone/master/phoneDir/" + fileName);
            Response<ResponseBody> response = call.execute();
            String json = response.body().string();

            List<PhoneBean> listBeen = PhoneBean.arrayListBeanFromData(json);
            return listBeen;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
