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
    public static List<PhoneBean> refresh(Context context, final String url)
    {
        try
        {
            RetrofitHelper.INetworkPort iNetworkPort = RetrofitHelper.init(context);
            Call<ResponseBody> call = iNetworkPort.getList(url);
            Response<ResponseBody> response = call.execute();
            String json = response.body().string();

            List<PhoneBean> listBeen = PhoneBean.arrayListBeanFromData(json);
            if(listBeen!=null&&listBeen.size()!=0)
            {
                for (PhoneBean phoneBean:listBeen)
                {
                    String number = phoneBean.getNumber();
                    if (number !=null)
                    {
                        /*去掉空格、换行*/
                        phoneBean.setNumber(number.replace(" ", "").replace("\r\n","").replace("\r","").replace("\n",""));
                    }
                }
            }

            return listBeen;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TargetBean> getUrlList(Context context, final String fileName)
    {
        try
        {
            RetrofitHelper.INetworkPort iNetworkPort = RetrofitHelper.init(context);
            Call<ResponseBody> call = iNetworkPort.getList("https://raw.githubusercontent.com/tommyyi/CallPhone/master/guide/" + fileName);
            Response<ResponseBody> response = call.execute();
            String json = response.body().string();

            List<TargetBean> listBeen = TargetBean.arrayTargetBeanFromData(json);
            return listBeen;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
