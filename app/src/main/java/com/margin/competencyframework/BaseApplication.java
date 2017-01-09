package com.margin.competencyframework;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;

/**
 * Created by Martha on 1/8/2017.
 */
public class BaseApplication extends Application {

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    public synchronized Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
        return mTracker;
    }
}
