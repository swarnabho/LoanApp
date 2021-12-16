package com.textifly.quickmudra.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomPreference {
    private SharedPreferences mPreferences;
    Context mContext;

    public CustomPreference(Context mContext) {
        this.mContext = mContext;
    }

    public void write(String key, String putId, String value) {
        mPreferences = mContext.getSharedPreferences(key, 0);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.putString(putId, value);
        editor.apply();
    }

    public String read(String key, String putId) {
        mPreferences = mContext.getSharedPreferences(key, 0);
        return mPreferences.getString(putId, "");
    }

    public void clear(String key) {
        mPreferences = mContext.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
