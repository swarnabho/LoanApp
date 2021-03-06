package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityOtpValidationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpValidationActivity extends AppCompatActivity {
    ActivityOtpValidationBinding binding;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    // string for storing our verification ID
    private String verificationId;
    String phno = "";
    String strOTP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpValidationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phno = getIntent().getStringExtra("phno");
        Log.d("OTP PHONE",phno);

        mAuth = FirebaseAuth.getInstance();
        binding.tvText.setText("Please type the verification code sent to " + phno);

        initiateOtp();

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                strOTP = otp;
            }
        });

        binding.validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strOTP.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter otp", Toast.LENGTH_SHORT).show();
                }
                if (strOTP.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid otp", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, strOTP);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });

        /*mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                verificationId = verificationId;
                mResendToken = token;
            }
        };*/
    }

    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91 "+phno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // this function will call when sim is in same phone
                                //Log.d("TAGGGG","YESSSSSSS");
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                // this function will call when sim is in other phone
                                //super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(getIntent().getExtras().containsKey("from")){
                                //Toast.makeText(OtpValidationActivity.this, "FROM", Toast.LENGTH_SHORT).show();
                                if(getIntent().getStringExtra("from").equalsIgnoreCase("forgotPW")){
                                    Intent intent = new Intent(OtpValidationActivity.this,ForgotPWActivity.class);
                                    intent.putExtra("phno",phno);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                                    finish();
                                }else if(getIntent().getStringExtra("from").equalsIgnoreCase("alternate")){
                                    saveAlternateNo(phno);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Phone number authentication completed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OtpValidationActivity.this,RegistrationActivity.class);
                                intent.putExtra("phno",phno);
                                intent.putExtra("user_id", YoDB.getPref().read(Constants.ID,""));
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "error in verifying otp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveAlternateNo(String phno) {
        Log.d("user_id",YoDB.getPref().read(Constants.ID,""));
        CustomProgressDialog.showDialog(OtpValidationActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.DETAILS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(OtpValidationActivity.this,false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equalsIgnoreCase("0")){
                        Toast.makeText(OtpValidationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OtpValidationActivity.this,DetailsListActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(OtpValidationActivity.this,false);
                Toast.makeText(OtpValidationActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("alternate_contact","+91 "+phno);
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));

                return body;
            }
        };

        Volley.newRequestQueue(OtpValidationActivity.this).add(sr);
    }
    // [END sign_in_with_phone]

    private void updateUI(FirebaseUser user) {

    }


}