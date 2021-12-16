package com.textifly.quickmudra.Helper;

import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Utils.Constants;

public class ManageLoginData {
    public static void addLoginData(String id, String name, String phone) {
        YoDB.getPref().write(Constants.ID, "", id);
        YoDB.getPref().write(Constants.NAME, "", name);
        YoDB.getPref().write(Constants.PHONE, "", phone);
    }

    public static void clearLoginData() {
        //Login DB
        YoDB.getPref().clear(Constants.ID);
        YoDB.getPref().clear(Constants.NAME);
        YoDB.getPref().clear(Constants.PHONE);
        YoDB.getPref().clear(Constants.EMAIL);

    }
}
