package com.hsr.hemant.ppp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Windows on 30-01-2015.
 */
public class MyApplication extends Application {


    private static MyApplication sInstance;

    public double latis[];
    public double longis[];
    public DisplayMetrics metrics;
    public String title[];
    public Bitmap bms[];
    public Uri userPhotoUri;
    public Location myLocation;
    public String urls[];

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public Uri getUserPhotoUri() {
        return userPhotoUri;
    }

    public void setUserPhotoUri(Uri userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = this;

    }

    public DisplayMetrics getMetrics() {
        metrics = new DisplayMetrics();

        metrics = getResources().getDisplayMetrics();
        return metrics;
    }

    public double[] getLatis() {
        return latis;
    }

    public void setLatis(double[] latis) {
        this.latis = latis;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public double[] getLongis() {
        return longis;
    }

    public void setLongis(double[] longis) {
        this.longis = longis;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public Bitmap[] getBms() {
        return bms;
    }

    public void setBms(Bitmap[] bms) {
        this.bms = bms;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        latis = null;
        longis = null;
        bms = null;
        title = null;
    }

}
