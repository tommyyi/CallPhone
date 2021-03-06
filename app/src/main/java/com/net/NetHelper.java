package com.net;

import android.content.Context;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 */

public class NetHelper
{
    public static List<PhoneBean> getRemotePhoneBeanList(Context context, final String url)
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

    /**
     * @param context
     * @param fileName company的配置文件，里面是一个json数组，每个user使用数组中指定位置的一个元素，这个元素是要一个url，通过这个url可以获取到该用户需要的编码列表
     * @return url列表
     */
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
