package com.example.ciyashop.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.model.Customer;
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

public class ChangePasswordActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etOldPassword)
    EditTextRegular etOldPassword;

    @BindView(R.id.etNewPassword)
    EditTextRegular etNewPassword;

    @BindView(R.id.etConfirrmNewPassword)
    EditTextRegular etConfirrmNewPassword;

    @BindView(R.id.tvSave)
    TextViewRegular tvSave;

    @BindView(R.id.tvCancel)
    TextViewRegular tvCancel;

    @BindView(R.id.llButton)
    LinearLayout llButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setToolbarTheme();
        hideSearchNotification();
        setThemeColor();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.account_setting));
        showBackButton();
    }

    public void setThemeColor() {
        llButton.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }

    @OnClick(R.id.tvCancel)
    public void tvCancelClick() {
        finish();
    }

    @OnClick(R.id.tvSave)
    public void tvSaveClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {

                String cust = getPreferences().getString(RequestParamUtils.CUSTOMER, "");
                Customer customer = new Gson().fromJson(
                        cust, new TypeToken<Customer>() {
                        }.getType());

                if (etEmail.getText().toString().toLowerCase().equals(customer.email.toLowerCase())) {
                    if (etOldPassword.getText().toString().isEmpty()) {
                        Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    } else {
                        if (etOldPassword.getText().toString().equals(getPreferences().getString(RequestParamUtils.PASSWORD, ""))) {
                            if (etNewPassword.getText().toString().isEmpty()) {
                                Toast.makeText(this, R.string.enter_new_password, Toast.LENGTH_SHORT).show();
                            } else {
                                if (etConfirrmNewPassword.getText().toString().isEmpty()) {
                                    Toast.makeText(this, R.string.enter_confirm_password, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (etNewPassword.getText().toString().equals(etConfirrmNewPassword.getText().toString())) {
                                        //change Password

                                        changePassword();

                                    } else {
                                        Toast.makeText(this, R.string.password_and_confirm_password_not_matched, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.enter_valid_email_address, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void changePassword() {
        PostApi postApi = new PostApi(this, "reset_password", this);
        try {
            JSONObject jsonObject = new JSONObject();

            String id = getPreferences().getString(RequestParamUtils.ID, "");
            jsonObject.put("user_id", id);
            jsonObject.put("password", etNewPassword.getText().toString());

            postApi.callPostApi(new URLS().RESET_PASSWORD, jsonObject.toString());
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public void onResponse(final String response, String methodName) {

        if (methodName.equals("reset_password")) {

            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {

                        SharedPreferences.Editor pre = getPreferences().edit();
                        pre.putString(RequestParamUtils.PASSWORD, etNewPassword.getText().toString());
                        pre.commit();

                        etEmail.setText("");
                        etOldPassword.setText("");
                        etNewPassword.setText("");
                        etConfirrmNewPassword.setText("");

                        Toast.makeText(this, R.string.information_updated_successfully, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }

}
