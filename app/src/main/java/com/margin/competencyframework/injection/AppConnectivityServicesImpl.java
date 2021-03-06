package com.margin.competencyframework.injection;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.margin.competencyframework.model.APIError;
import com.margin.competencyframework.rest.RestAPI;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martha on 1/8/2017.
 */
public class AppConnectivityServicesImpl implements AppConnectivityServices{

    public static final String RETROFIT_ADAPTER_ORDINARY = "ordinary";
    public static final String RETROFIT_ADAPTER_REACTIVE = "reactive";
    private static final int DURATION_CONNECTION_TIMEOUT = 10;
    private static final int DURATION_WRITE_TIMEOUT = 10;
    private static final int DURATION_READ_TIMEOUT = 30;
    private Context mContext;

    public AppConnectivityServicesImpl(Context context) {
        this.mContext = context;
    }

    /**
     * @return The context of {@link com.margin.competencyframework.rest.RestAPI
     * LipstickApplication}.
     */
    @Provides
    @Singleton
    private Context provideContext() {
        return mContext;
    }

    /**
     * @param context The context of {@link com.margin.competencyframework.rest.RestAPI
     *                LipstickApplication}.
     * @return {@link RequestManager} for interacting with {@link Glide}.
     */
    @Provides
    @Singleton
    public RequestManager provideGlide(Context context) {
        return Glide.with(context);
    }

    /**
     * @return {@link OkHttpClient} instance with custom options applied.
     */
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DURATION_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DURATION_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DURATION_READ_TIMEOUT, TimeUnit.SECONDS);
        return builder.build();
    }

    /**
     * @return {@link Retrofit} instance constructed with default {@link retrofit2.CallAdapter
     * CallAdapter}.
     */
    @Provides
    @Singleton
    @Named(RETROFIT_ADAPTER_ORDINARY)
    public Retrofit providesOrdinaryRetrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(RestAPI.ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @return {@link Retrofit} instance constructed with {@link RxJavaCallAdapterFactory}.
     */
    @Provides
    @Singleton
    @Named(RETROFIT_ADAPTER_REACTIVE)
    public Retrofit providesReactiveOrdinaryRetrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(RestAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @param retrofit A {@link Retrofit} instance, which provided the mError body.
     * @return A {@link Converter}, that serializes {@code retrofit}'s response mError body to
     * {@link APIError} instance.
     */
    @Provides
    @Singleton
    public Converter<ResponseBody, APIError> providesErrorConverter(@Named(RETROFIT_ADAPTER_ORDINARY) Retrofit retrofit) {
        return retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
    }

    /**
     * @param retrofit {@link Retrofit} created with {@link retrofit2.CallAdapter CallAdapterFactory}.
     * @return {@link RestAPI} instance constructed with {@code retrofit}.
     */
    @Provides
    @Singleton
    @Named(RETROFIT_ADAPTER_ORDINARY)
    public RestAPI providesOrdinaryStrongLoopApi(@Named(RETROFIT_ADAPTER_ORDINARY) Retrofit retrofit) {
        return retrofit.create(RestAPI.class);
    }

    @Provides
    @Singleton
    @Named(RETROFIT_ADAPTER_REACTIVE)
    public RestAPI providesReactiveStrongLoopApi(@Named(RETROFIT_ADAPTER_REACTIVE) Retrofit retrofit) {
        return retrofit.create(RestAPI.class);
    }

    // TODO implement a builder class later


    @Override
    public Context getAppContext() {
        return null;
    }

    @Override
    public RequestManager getGlide() {
        return null;
    }

    @Override
    public RestAPI getOrdinaryRestApi() {
        return null;
    }

    @Override
    public RestAPI getReactiveRestApi() {
        return null;
    }

    @Override
    public Converter<ResponseBody, APIError> getErrorConverter() {
        return null;
    }
}
