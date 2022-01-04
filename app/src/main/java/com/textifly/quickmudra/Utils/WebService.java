package com.textifly.quickmudra.Utils;

import com.textifly.quickmudra.Model.ResponseDataModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebService {

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateVoter(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part voter_front,
            @Part MultipartBody.Part voter_back
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updatePan(
            @Part("user_id") RequestBody user_id,
            @Part("pan_no") RequestBody pan,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part pan_font,
            @Part MultipartBody.Part pan_back
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateAadhar(
            @Part("user_id") RequestBody user_id,
            @Part("aadhar_no") RequestBody aadhar,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part aadhar_font,
            @Part MultipartBody.Part aadhar_back
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updatePassport(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part passport
            );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateDriving(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part driving
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateAddressProof(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part addressProof
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateMarksheet(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part marksheet
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateCollegeId(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part collegeId
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateSignature(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part signature
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateSelfie(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part selfie
    );

    @Multipart
    @POST("details_update")
    Call<ResponseDataModel> updateSelfieVideo(
            @Part("user_id") RequestBody user_id,
            @Part("other_percentage") RequestBody other_percentage,
            @Part MultipartBody.Part profile_video
    );
}
