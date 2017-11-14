package com.jyx.android.net;


import com.jyx.android.base.UserRecord;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class ApiManager {
    private static final String SERVER_URL = "http://120.76.131.75/JYX/";
    //private static final String SERVER_URL = "http://120.76.41.244:8888/JYX/";
    //private static final String SERVER_URL = "http://61.166.10.79:9090/JYX/";

    private JYXApi mApi;

    private ApiManager(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(new JYXCookieJar()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(JYXApi.class);
    }

    private static class InstanceHolder{
        static final ApiManager API_MANAGER = new ApiManager();
    }


    public static JYXApi getApi(){
        return InstanceHolder.API_MANAGER.mApi;
    }

    private static class JYXCookieJar implements CookieJar{

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            UserRecord.getInstance().setCookies(cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if(UserRecord.getInstance().getCookies() != null){
                return UserRecord.getInstance().getCookies();
            }
            return new ArrayList<>();
        }
    }



}
