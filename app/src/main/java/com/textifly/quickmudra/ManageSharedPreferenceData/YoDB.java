package com.textifly.quickmudra.ManageSharedPreferenceData;

import android.app.Application;
import android.content.Context;

import com.textifly.quickmudra.Utils.CustomPreference;

public class YoDB extends Application {
    private static Context mContext;
    private static CustomPreference sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static CustomPreference getPref() {
        if (sharedPreferences == null) {
            sharedPreferences = new CustomPreference(mContext);
        }
        return sharedPreferences;
    }

}
