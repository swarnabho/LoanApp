package com.textifly.quickmudra.Utils;

public class Urls {
    public static String MAIN_BASEURL = "https://adminquickmudra.org.in/";
    public static String BASEURL = MAIN_BASEURL + "api/";

    /**
     * @Post : POST
     * @Name : registration
     * @Parameter : fname,email,mobile,confirm_password,promo_code,imei_no
     */
    public static final String REGISTER = BASEURL + "registration";
}
