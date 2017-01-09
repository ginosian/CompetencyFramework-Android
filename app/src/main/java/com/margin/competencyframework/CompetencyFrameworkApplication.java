package com.margin.competencyframework;

import com.margin.competencyframework.injection.AppConnectivityServices;
import com.margin.competencyframework.injection.AppConnectivityServicesImpl;

/**
 * Created by Martha on 1/8/2017.
 */
public class CompetencyFrameworkApplication extends BaseApplication{

    private static AppConnectivityServices appConnectivityServices;

    public static AppConnectivityServices appConnectivityServices() {
        return appConnectivityServices;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO implement a builder class later
        appConnectivityServices = new AppConnectivityServicesImpl(this);
    }
}
