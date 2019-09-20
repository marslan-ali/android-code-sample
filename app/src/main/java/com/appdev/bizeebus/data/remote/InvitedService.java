package com.appdev.bizeebus.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import co.appdev.invited.util.ConstantUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


interface BizeeService {

    String ENDPOINT = "http://" + ConstantUtils.HOST_NAME ;

//    @FormUrlEncoded
//    @POST("user/register")
//    Observable<DefaultResponse> userSignup(@Field("device_token") String device_token, @Field("device_type") String deviceType, @Field("email") String email, @Field("password") String password, @Field("zip_code") String zipCode, @Field("route_number") String routeNumber, @Field("address") String address, @Field("child_name") String childName, @Field("dispatcher_pin") String pinCode);

     class Creator {

        public static BizeeService newBizeeBusServices() {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            UnauthorisedInterceptor customInterceptor = new UnauthorisedInterceptor();
            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(customInterceptor)
                    .addInterceptor(interceptor)
//                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BizeeService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(BizeeService.class);
        }
    }
}

