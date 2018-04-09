package com.net;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * retrofit帮助类
 * 【1】使用自定义的okhttp客户端，此okhttp客户端可以自定义cache之类的特性
 * 【2】添加stetho的网络拦截器
 */

public class RetrofitHelper
{
    private static final String BASE_URL = "https://github.com";
    private static INetworkPort iNetWorkPort;

    /*返回retrofit 网络接口，retrofit会依据interface动态生成实现了该interface的类的对象*/
    public static INetworkPort init(Context context)
    {
        if (iNetWorkPort == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (iNetWorkPort == null)
                    iNetWorkPort = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpFactory(context).getOkHttpClient()).build().create(INetworkPort.class);//依据interface动态生成实现了该interface的类的对象
            }
        }
        return iNetWorkPort;
    }

    private static class OkHttpFactory
    {
        private OkHttpClient client;
        private static final int TIMEOUT_READ = 25;
        private static final int TIMEOUT_CONNECTION = 25;

        Cache mCache;

        private OkHttpFactory(Context context)
        {
            mCache = new Cache(context.getCacheDir(), 10 * 1024 * 1024);//缓存目录
            OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(mCache).retryOnConnectionFailure(true) //失败重连
                                                                     .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)//读超时设置
                                                                     .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);//连接超时设置
                /*反射获取stetho网络拦截器*/
            Interceptor interceptor = StetherHelper.getStethoOkHttp3Interceptor();
                /*添加stetho的网络拦截器*/
            if (interceptor != null)
            {
                builder.addNetworkInterceptor(interceptor);
            }
            client = builder.build();
        }

        OkHttpClient getOkHttpClient()
        {
            return client;
        }
    }

    /**
     * retrofit接口定义
     */
    public interface INetworkPort
    {
        /**
         * @param url
         * @return
         */
        @GET
        public Call<ResponseBody> getList(@Url String url);
    }
}
