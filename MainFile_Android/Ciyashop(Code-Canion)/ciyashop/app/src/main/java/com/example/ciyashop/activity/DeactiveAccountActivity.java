package com.example.ciyashop.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.Utils;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeactiveAccountActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etPassword)
    EditTextRegular etPassword;

    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;

    @BindView(R.id.llButton)
    LinearLayout llButton;

    public String socialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactive_account);
        setScreenLayoutDirection();
        ButterKnife.bind(this);
        setToolbarTheme();
        setThemeColor();
        hideSearchNotification();
        settvTitle(getResources().getString(R.string.account_setting));
        showBackButton();

        socialLogin = getPreferences().getString(RequestParamUtils.SOCIAL_SIGNIN, "");

        if (socialLogin.equals("1")) {
            tilPassword.setVisibility(View.GONE);
        } else {
            tilPassword.setVisibility(View.VISIBLE);

        }

    }
    public void setThemeColor() {
        llButton.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }


    @OnClick(R.id.tvConfirmDeactivation)
    public void tvConfirmDeactivationClick() {
        if (etEmail.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                if (socialLogin.equals("1")) {
                    saveData();
                } else {
                    if (etPassword.getText().toString().isEmpty()) {
                        Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    } else {
                        saveData();
                    }
                }

            } else {
                Toast.makeText(this, R.string.enter_valid_email_address, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveData() {
        PostApi postApi = new PostApi(this, "deactivate_user", this);
        try {
            JSONObject jsonObject = new JSONObject();

            String id = getPreferences().getString(RequestParamUtils.ID, "");
            jsonObject.put("user_id", id);
            jsonObject.put("disable_user", "1");
            jsonObject.put("email", etEmail.getText().toString());

            if (socialLogin.equals("1")) {
                jsonObject.put("password", "");
                jsonObject.put("social_user", "yes");
            } else {
                jsonObject.put("password", etPassword.getText().toString());
            }

            postApi.callPostApi(new URLS().DEACTIVATE_USER, jsonObject.toString());
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public void onResponse(final String response, String methodName) {

        if (methodName.equals("deactivate_user")) {

            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        SharedPreferences.Editor pre = getPreferences().edit();
                        pre.putString(RequestParamUtils.CUSTOMER, "");
                        pre.putString(RequestParamUtils.ID, "");
                        pre.commit();
                        Toast.makeText(this, R.string.account_deactivated, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
