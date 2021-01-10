package com.ashtray.nativead;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BaseApplication extends Application {

    private static BaseApplication application;

    public static BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        enableLogPrinting = application.getString(R.string.want_to_enable_log).equals("yes");
        loadOnlyTestAds = application.getString(R.string.loading_ad_type).equals("test");
    }

    private boolean enableLogPrinting = true;
    private boolean loadOnlyTestAds = true;

    public boolean enableLogPrinting() {
        return enableLogPrinting;
    }

    public boolean loadOnlyTestAds() {
        return loadOnlyTestAds;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}