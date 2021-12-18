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


    /**
     * @Post : POST
     * @Name : login
     * @Parameter : mobile,password
     */
    public static final String LOGIN = BASEURL + "login";

    /**
     * @Post : POST
     * @Name : details_update
     * @Parameter : user_id,fname,lname,father_name,mother_name,address,permannet_address,gender,dob,college,institute_pincode,course_name,course_start_year,course_end_year,exam_year,percentage,aadhar_no,aadhar_back,aadhar_front,voter_front,voter_back,pan_no,pan_front,pan_rear,driving_license,marksheet_image,college_id,signature,address_proof,profile_photo,profile_video
     */
    public static final String DETAILS_UPDATE = BASEURL + "details_update";

}

