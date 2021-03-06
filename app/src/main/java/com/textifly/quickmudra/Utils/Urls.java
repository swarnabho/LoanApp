package com.textifly.quickmudra.Utils;

public class Urls {
    public static String MAIN_BASEURL = "https://adminquickmudra.org.in/";
    public static String BASEURL = MAIN_BASEURL + "api/";
    public static String IMAGE_URL = "https://adminquickmudra.org.in/storage/app/public/uploads/";

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

    /**
     * @Post : POST
     * @Name : userprofilecompletedcheck
     * @Parameter : userid, type = kyc/documents
     */
    public static final String USER_PROFILE_COMPLETE_CHECK = BASEURL + "userprofilecompletedcheck";

    /**
     * @Post : POST
     * @Name : mobilecheck
     * @Parameter : mobile
     */
    public static final String MOBILE_CHECK = BASEURL + "mobilecheck";

    /**
     * @Post : POST
     * @Name : userdetails
     * @Parameter : userid
     */
    public static final String USER_DETAILS = BASEURL + "userdetails";

    /**
     * @Post : POST
     * @Name : usercheck
     * @Parameter : userid
     */
    public static final String USER_CHECK = BASEURL + "usercheck";

    /**
     * @Post : POST
     * @Name : forgetpassword
     * @Parameter : mobile,password
     */
    public static final String FORGET_PW = BASEURL + "forgetpassword";

    /**
     * @Post : POST
     * @Name : addBankDetails
     * @Parameter : user_id,payment_type,account_no,branch_name,ifsc_code,upi_id
     */
    public static final String ADD_BANK_DETAILS = BASEURL + "addBankDetails";

    /**
     * @Post : POST
     * @Name : personalDetails
     * @Parameter : user_id
     */
    public static final String PERSONAL_DETAILS = BASEURL + "personalDetails";

    /**
     * @Post : POST
     * @Name : educationalDetailsFetch
     * @Parameter : user_id
     */
    public static final String EDUCATIONAL_DETAILS_FETCH = BASEURL + "educationalDetailsFetch";

    /**
     * @Post : POST
     * @Name : uploadedDocumentsFetch
     * @Parameter : user_id
     */
    public static final String UPLOADED_DOCUMENT_FETCH = BASEURL + "uploadedDocumentsFetch";

    /**
     * @Post : POST
     * @Name : getBankDetails
     * @Parameter : user_id
     */
    public static final String GET_BANK_DETAILS = BASEURL + "getBankDetails";

    /**
     * @Post : POST
     * @Name : getProcessDetails
     * @Parameter : amount,tenure
     */
    public static final String GET_PROCESS_DETAILS = BASEURL + "getProcessDetails";

    /**
     * @Post : POST
     * @Name : pendingRequest
     * @Parameter : user_id
     */
    public static final String PENDING_REQUEST = BASEURL + "pendingRequest";

    /**
     * @Post : POST
     * @Name : dueRequest
     * @Parameter : user_id
     */
    public static final String DUE_REQUEST = BASEURL + "dueRequest";

    /**
     * @Post : POST
     * @Name : loanRequest
     * @Parameter : user_id,amount,tenure,total_fee,repayment_amount,processing_fee
     */
    public static final String LOAN_REQUEST = BASEURL + "loanRequest";

    /**
     * @Post : POST
     * @Name : paidLoans
     * @Parameter : user_id
     */
    public static final String PAID_LOANS = BASEURL + "paidLoans";

    /**
     * @Post : POST
     * @Name : orderDetails
     * @Parameter : loans_id
     */
    public static final String ORDER_DETAILS = BASEURL + "orderDetails";


    /**
     * @Post : POST
     * @Name : profileUpdate
     * @Parameter : user_id,fname,permanent_address,mobile,email,profile_photo
     */
    public static final String PROFILE_UPDATE = BASEURL + "profileUpdate";


    /**
     * @Post : POST
     * @Name : changePassword
     * @Parameter : user_id,confirm_password
     */
    public static final String CHANGE_PASSWORD = BASEURL + "changePassword";

    /**
     * @Post : POST
     * @Name : payment
     * @Parameter : loans_id
     */
    public static final String PAYMENT = BASEURL + "payment";

    /**
     * @Post : POST
     * @Name : mailVerifyRequest
     * @Parameter : user_id,email_id
     */
    public static final String MAIL_VERIFY_REQUEST = BASEURL + "mailVerifyRequest";

    /**
     * @Post : POST
     * @Name : getdocumentsstatus
     * @Parameter : user_id
     */
    public static final String GET_DOCUMENT_STATUS = BASEURL + "getdocumentsstatus";

}

