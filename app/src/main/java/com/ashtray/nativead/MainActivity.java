package com.ashtray.nativead;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.ashtray.nativeadsmall.NativeTemplateStyle;
import com.ashtray.nativeadsmall.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "[MainActivity]";

    private UnifiedNativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAdSdk();
        loadSmallNativeAd();
    }

    private void initializeAdSdk() {
        //initializing ads sdk
        MobileAds.initialize(this, status-> LogManager.d(DEBUG_TAG, "Mobile ads initialized"));

        //adding test configuration
        List<String> testDeviceIDList = Collections.singletonList("A477C76505B9E3FF986892985397E14C");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIDList).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    private void loadSmallNativeAd() {
        //hiding the ad view first
        findViewById(R.id.ad_placeholder).setVisibility(View.GONE);

        //checking internet available or not to show ad
        if(!BaseApplication.getInstance().isInternetAvailable()) {
            LogManager.e(DEBUG_TAG, "Couldn't show ad, Internet not found [return]");
            return;
        }
        
        LogManager.e(DEBUG_TAG, "loading ad..");
        findViewById(R.id.ad_placeholder).setVisibility(View.VISIBLE);
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    // If this callback occurs after the activity is destroyed, you must call
                    // destroy and return or you may get a memory leak.
                    boolean isDestroyed = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = isDestroyed();
                    }
                    if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                        unifiedNativeAd.destroy();
                        LogManager.e(DEBUG_TAG, "Activity destroyed already");
                        return;
                    }
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        LogManager.e(DEBUG_TAG, "Destroying previous ad to show a new one");
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    TemplateView template = findViewById(R.id.ad_template);
                    template.setStyles(styles);
                    template.setNativeAd(unifiedNativeAd);
                })
                .withAdListener(new AdListener(){
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        LogManager.e(DEBUG_TAG, "Failed to load an ad [reason = " + loadAdError.getMessage() + "]");
                        findViewById(R.id.ad_placeholder).setVisibility(View.GONE);
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadNewAdClicked(View view) {
        loadSmallNativeAd();
    }
}