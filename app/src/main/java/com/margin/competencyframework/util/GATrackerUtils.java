package com.margin.competencyframework.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.margin.competencyframework.BaseApplication;

/**
 * Created by Martha on 1/8/2017.
 */
public class GATrackerUtils {

    /**
     * Send screen names to Google Analytics.
     *
     * @param name The name of the screen
     */
    public static void trackScreenName(Context context, String name) {
        Tracker tracker = getDefaultTracker(context);
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Send events to Google Analytics.
     *
     * @param category The event category
     * @param action   The event action
     * @param label    The event label (optional)
     * @param value    The event value (optional)
     */
    public static void trackEvent(Context context, @NonNull String category, @NonNull String action,
                                  String label, long value) {
        Tracker tracker = getDefaultTracker(context);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    /**
     * Send timed events to Google Analytics.
     *
     * @param category The category of the timed event
     * @param value    The timing measurement in milliseconds
     * @param name     The name of the timed event (optional)
     * @param label    The label of the timed event (optional)
     */
    public static void trackTimedEvent(Context context, @NonNull String category, long value,
                                       String name, String label) {
        Tracker tracker = getDefaultTracker(context);
        tracker.send(new HitBuilders.TimingBuilder()
                .setCategory(category)
                .setValue(value)
                .setVariable(name)
                .setLabel(label)
                .build());
    }

    /**
     * Send exceptions to Google Analytics.
     *
     * @param exception The {@link Throwable} exception that was caught
     */
    public static void trackException(Context context, Throwable exception) {
        Tracker tracker = getDefaultTracker(context);
        tracker.send(new HitBuilders.ExceptionBuilder()
                // Using StandardExceptionParser to get an Exception description.
                .setDescription(new StandardExceptionParser(context, null)
                        .getDescription(Thread.currentThread().getName(), exception))
                .setFatal(false).build());
    }

    /**
     * Check if main application class extends BaseApplication
     *
     * @return default tracker object from BaseApplication
     */
    private static Tracker getDefaultTracker(Context context) {
        try {
            BaseApplication baseApplication = (BaseApplication) context.getApplicationContext();
            return baseApplication.getDefaultTracker();
        } catch (ClassCastException e) {
            throw new ClassCastException("Your main application class must extend" +
                    " BaseApplication!");
        }
    }
}
