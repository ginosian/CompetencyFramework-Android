package com.margin.competencyframework.injection;

import android.content.Context;

import com.bumptech.glide.RequestManager;
import com.margin.competencyframework.model.APIError;
import com.margin.competencyframework.rest.RestAPI;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Martha on 1/8/2017.
 */
@Singleton
@Component(modules = {AppConnectivityServicesImpl.class})
public interface AppConnectivityServices {

    /**
     * @return Application context.
     */
    Context getAppContext();

    /**
     * @return {@link RequestManager} for loading images with {@link com.bumptech.glide.Glide Glide}.
     */
    RequestManager getGlide();

    /**
     * @return Singleton {@link com.margin.competencyframework.rest.RestAPI} instance where {@link retrofit2.Retrofit} is
     * constructed with {@link retrofit2.CallAdapter CallAdapter}.
     */
    @Named(AppConnectivityServicesImpl.RETROFIT_ADAPTER_ORDINARY)
    RestAPI getOrdinaryRestApi();

    /**
     * @return Singleton {@link RestAPI} instance where {@link retrofit2.Retrofit} is
     * constructed with {@link retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
     * RxJavaCallAdapterFactory}.
     */
    @Named(AppConnectivityServicesImpl.RETROFIT_ADAPTER_REACTIVE)
    RestAPI getReactiveRestApi();

    /**
     * @return Singleton {@link RestAPI} mError response converter.
     */
    Converter<ResponseBody, APIError> getErrorConverter();

//    /**
//     * Injects {@link DatabaseConnector} to use the {@link com.squareup.sqlbrite.BriteDatabase}
//     */
//    void inject(DatabaseConnector databaseConnector);
}
