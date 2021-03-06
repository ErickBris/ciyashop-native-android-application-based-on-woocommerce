package com.example.ciyashop.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ciyashop.Manifest;
import com.example.ciyashop.R;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.Utils;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.etName)
    EditTextRegular etName;

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etContactNumber)
    EditTextRegular etContactNumber;

    @BindView(R.id.etSubject)
    EditTextRegular etSubject;

    @BindView(R.id.etMessage)
    EditTextRegular etMessage;

    @BindView(R.id.ivPinterest)
    ImageView ivPinterest;

    @BindView(R.id.ivTwitter)
    ImageView ivTwitter;

    @BindView(R.id.ivFacebook)
    ImageView ivFacebook;

    @BindView(R.id.ivLinkedin)
    ImageView ivLinkedin;

    @BindView(R.id.ivGooglePlus)
    ImageView ivGooglePlus;

    @BindView(R.id.tvSend)
    TextViewBold tvSend;

    @BindView(R.id.Logo)
    ImageView Logo;


    @BindView(R.id.ivPhone)
    ImageView ivPhone;

    @BindView(R.id.ivEmail)
    ImageView ivEmail;

    @BindView(R.id.ivLocation)
    ImageView ivLocation;

    @BindView(R.id.ivInstagram)
    ImageView ivInstagram;

    @BindView(R.id.tvPhone)
    TextViewLight tvPhone;

    @BindView(R.id.tvEmail)
    TextViewLight tvEmail;

    @BindView(R.id.tvLocation1)
    TextViewLight tvLocation1;

    @BindView(R.id.tvLocation2)
    TextViewLight tvLocation2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        setToolbarTheme();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.contact_us));
        hideSearchNotification();
        showBackButton();

        if (Constant.PHONE.length() == 0) {
            tvPhone.setVisibility(View.GONE);
            ivPhone.setVisibility(View.GONE);
        } else {
            tvPhone.setText(Constant.PHONE);
        }

        if (Constant.EMAIL.length() == 0) {
            tvEmail.setVisibility(View.GONE);
            ivEmail.setVisibility(View.GONE);
        } else {
            tvEmail.setText(Constant.EMAIL);
        }

        if (Constant.ADDRESS_LINE1.length() == 0 && Constant.ADDRESS_LINE2.length() == 0) {
            ivLocation.setVisibility(View.GONE);
            tvLocation1.setVisibility(View.GONE);
            tvLocation2.setVisibility(View.GONE);
        } else {
            if (Constant.ADDRESS_LINE1.length() == 0) {
                tvLocation1.setVisibility(View.GONE);
            } else {
                tvLocation1.setText(Constant.ADDRESS_LINE1);
            }
            if (Constant.ADDRESS_LINE2.length() == 0) {
                tvLocation2.setVisibility(View.GONE);
            } else {
                tvLocation2.setText(Constant.ADDRESS_LINE2);
            }
        }
        if (Constant.SOCIALLINK.pinterest.length() == 0) {
            ivPinterest.setVisibility(View.GONE);
        }
        if (Constant.SOCIALLINK.twitter.length() == 0) {
            ivTwitter.setVisibility(View.GONE);
        }
        if (Constant.SOCIALLINK.facebook.length() == 0) {
            ivFacebook.setVisibility(View.GONE);
        }
        if (Constant.SOCIALLINK.linkedin.length() == 0) {
            ivLinkedin.setVisibility(View.GONE);
        }
        if (Constant.SOCIALLINK.googlePlus.length() == 0) {
            ivGooglePlus.setVisibility(View.GONE);
        }

        if (Constant.SOCIALLINK.instagram.length() == 0) {
            ivInstagram.setVisibility(View.GONE);
        }

        if (Constant.APPLOGO != null && !Constant.APPLOGO.equals("")) {
            Picasso.with(this).load(Constant.APPLOGO).error(R.drawable.logo).into(Logo);
        }
        setColorTheme();
    }

    @OnClick(R.id.ivPhone)
    public void ivPhoneClick() {
        phone();
    }

    @OnClick(R.id.tvPhone)
    public void tvPhoneClick() {
        phone();
    }

    @OnClick(R.id.ivEmail)
    public void ivEmailClick() {
        email();
    }

    @OnClick(R.id.tvEmail)
    public void tvEmailClick() {
        email();
    }

    public void email() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + tvEmail.getText().toString()));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }

    public void phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tvPhone.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.ivPinterest)
    public void ivPinterestClick() {
        String url = Constant.SOCIALLINK.pinterest;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void setColorTheme() {
        ivLocation.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivPhone.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivFacebook.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivGooglePlus.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivLinkedin.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivTwitter.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivPinterest.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivInstagram.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivEmail.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));

        tvSend.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }

    @OnClick(R.id.ivTwitter)
    public void ivTwitterClick() {
        String url = Constant.SOCIALLINK.twitter;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.ivInstagram)
    public void ivInstagramClick() {
        String url = Constant.SOCIALLINK.instagram;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.ivFacebook)
    public void ivFacebookClick() {
        String url = Constant.SOCIALLINK.facebook;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.ivLinkedin)
    public void ivLinkedinClick() {
        String url = Constant.SOCIALLINK.linkedin;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.ivGooglePlus)
    public void ivGooglePlusClick() {
        String url = Constant.SOCIALLINK.googlePlus;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.tvSend)
    public void tvSendClick() {
        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
        } else {
            if (etEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isValidEmail(etEmail.getText().toString())) {
                    if (etSubject.getText().toString().isEmpty()) {
                        Toast.makeText(this, R.string.enter_subject, Toast.LENGTH_SHORT).show();
                    } else {
                        if (etMessage.getText().toString().isEmpty()) {
                            Toast.makeText(this, R.string.enter_message, Toast.LENGTH_SHORT).show();
                        } else {
                            contactUs();
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.enter_valid_email_address, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void contactUs() {
        PostApi postApi = new PostApi(this, "contactus", this);
        JSONObject object = new JSONObject();
        try {

            object.put("name", etName.getText().toString());
            object.put("email", etEmail.getText().toString());
            object.put("contact_no", etContactNumber.getText().toString());
            object.put("subject", etSubject.getText().toString());
            object.put("message", etMessage.getText().toString());
            postApi.callPostApi(new URLS().CONTACTUS, object.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName) {

        if (methodName.equals("contactus")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    //set call here
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(this, R.string.message_sent_successfully, Toast.LENGTH_SHORT).show();
                        etName.getText().clear();
                        etEmail.getText().clear();
                        etContactNumber.getText().clear();
                        etSubject.getText().clear();
                        etMessage.getText().clear();

                    } else {
                        Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                    Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show(); //display in long period of time
            }
        }
    }

}
