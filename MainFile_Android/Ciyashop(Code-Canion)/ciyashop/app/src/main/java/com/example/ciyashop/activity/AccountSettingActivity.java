package com.example.ciyashop.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.model.Customer;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSettingActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.etFirstName)
    EditTextRegular etFirstName;

    @BindView(R.id.etLastName)
    EditTextRegular etLastName;

    @BindView(R.id.etDOB)
    EditTextRegular etDOB;

    @BindView(R.id.etMobileNumber)
    EditTextRegular etMobileNumber;

    @BindView(R.id.tvEmail)
    TextViewRegular tvEmail;

    @BindView(R.id.tvSave)
    TextViewRegular tvSave;

    @BindView(R.id.flMale)
    FrameLayout flMale;

    @BindView(R.id.flFemale)
    FrameLayout flFemale;

    @BindView(R.id.ivRightMale)
    ImageView ivRightMale;

    @BindView(R.id.ivRightFemale)
    ImageView ivRightFemale;

    @BindView(R.id.tvChangePassword)
    TextViewBold tvChangePassword;

    @BindView(R.id.llPassword)
    LinearLayout llPassword;

    private Customer customer = new Customer();

    DatePickerDialog datePickerDialog;
    private boolean allowClose = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        setToolbarTheme();
        setThemeColor();
        hideSearchNotification();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.account_setting));
        showBackButton();
        setData();
    }

    public void setThemeColor() {
        tvSave.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }

    public void setData() {

        String cust = getPreferences().getString(RequestParamUtils.CUSTOMER, "");
        customer = new Gson().fromJson(
                cust, new TypeToken<Customer>() {
                }.getType());

        String socialLogin = getPreferences().getString(RequestParamUtils.SOCIAL_SIGNIN, "");

        if (socialLogin.equals("1")) {
            llPassword.setVisibility(View.GONE);
        } else {
            llPassword.setVisibility(View.VISIBLE);
        }
        if (customer.id == 0) {
            //no data
        } else {
            //setData
            etFirstName.setText(customer.firstName + "");
            etLastName.setText(customer.lastName + "");
//            if (customer.dob != null) {
//                etDOB.setText(customer.dob + "");
//            }
//            etMobileNumber.setText(customer.billing.phone + "");
            tvEmail.setText(customer.email + "");
            try {
                JSONObject jsonObject = new JSONObject(cust);
                JSONArray jsonArray = jsonObject.getJSONArray("meta_data");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jb = jsonArray.getJSONObject(i);
                    if (jb.getString("key").toLowerCase().equals("mobile")) {
                        //mobile
                        etMobileNumber.setText(jb.getString("value"));
                    } else if (jb.getString("key").toLowerCase().equals("gender")) {
                        //gender

                        if (jb.getString("value").toLowerCase().equals("male")) {
                            GradientDrawable gradientDrawable = (GradientDrawable) flMale.getBackground();
                            gradientDrawable.setColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
                            gradientDrawable = (GradientDrawable) flFemale.getBackground();
                            gradientDrawable.setColor(Color.parseColor("#c5c5c5"));

//                            flMale.setBackgroundResource(R.drawable.primary_round_button);
                            ivRightFemale.setVisibility(View.GONE);
                            ivRightMale.setVisibility(View.VISIBLE);
                        } else if (jb.getString("value").toLowerCase().equals("female")) {
                            GradientDrawable gradientDrawable = (GradientDrawable) flFemale.getBackground();
                            gradientDrawable.setColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
                            gradientDrawable = (GradientDrawable) flMale.getBackground();
                            gradientDrawable.setColor(Color.parseColor("#c5c5c5"));

//                            flMale.setBackgroundResource(R.drawable.gray_round_corner_button);
                            ivRightFemale.setVisibility(View.VISIBLE);
                            ivRightMale.setVisibility(View.GONE);
                        } else {
                            flFemale.setBackgroundResource(R.drawable.gray_round_corner_button);
                            flMale.setBackgroundResource(R.drawable.gray_round_corner_button);
                            ivRightFemale.setVisibility(View.GONE);
                            ivRightMale.setVisibility(View.GONE);
                        }
                    } else if (jb.getString("key").toLowerCase().equals("dob")) {
                        //DOB
                        etDOB.setText(jb.getString("value"));
                    }
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }

    }

    @OnClick(R.id.flMale)
    public void flMaleClick() {
        GradientDrawable gradientDrawable = (GradientDrawable) flMale.getBackground();
        gradientDrawable.setColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        gradientDrawable = (GradientDrawable) flFemale.getBackground();
        gradientDrawable.setColor(Color.parseColor("#c5c5c5"));

//        flFemale.setBackgroundResource(R.drawable.gray_round_corner_button);
//        flMale.setBackgroundResource(R.drawable.primary_round_button);
        ivRightFemale.setVisibility(View.GONE);
        ivRightMale.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.flFemale)
    public void flFemaleClick() {
        GradientDrawable gradientDrawable = (GradientDrawable) flFemale.getBackground();
        gradientDrawable.setColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        gradientDrawable = (GradientDrawable) flMale.getBackground();
        gradientDrawable.setColor(Color.parseColor("#c5c5c5"));
//        flFemale.setBackgroundResource(R.drawable.primary_round_button);
//        flMale.setBackgroundResource(R.drawable.gray_round_corner_button);
        ivRightFemale.setVisibility(View.VISIBLE);
        ivRightMale.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvDeactivateAccount)
    public void tvDeactivateAccountClick() {
        Intent intent = new Intent(AccountSettingActivity.this, DeactiveAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvChangePassword)
    public void tvChangePasswordClick() {
        Intent intent = new Intent(AccountSettingActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.etDOB)
    public void etDOBClick() {
        //select date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final int minYear = mYear - 8;
        final int minMonth = mMonth;
        final int minDay = mDay;

        if (etDOB.getText().toString().equals("")) {
            mYear = minYear;
            mMonth = minMonth;
            mDay = minDay;
        } else {
            String selectedDate = etDOB.getText().toString();
            String[] dateParts = selectedDate.split("/");
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];

            mYear = Integer.parseInt(year);
            mMonth = Integer.parseInt(month) - 1;
            mDay = Integer.parseInt(day);
        }
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
            }
        };

        datePickerDialog = new DatePickerDialog(
                AccountSettingActivity.this, datePickerListener,
                mYear, mMonth, mDay) {
            @Override
            public void onBackPressed() {
                allowClose = true;
                super.onBackPressed();
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    DatePicker datePicker = datePickerDialog
                            .getDatePicker();

                    if (datePicker.getYear() < minYear || datePicker.getMonth() < minMonth && datePicker.getYear() == minYear ||
                            datePicker.getDayOfMonth() <= minDay && datePicker.getYear() == minYear && datePicker.getMonth() == minMonth) {

                        datePicker.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        etDOB.setText(datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                        allowClose = true;

                    } else {
                        allowClose = false;
                        Toast.makeText(AccountSettingActivity.this, R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        allowClose = true;
                    }
                }
                super.onClick(dialog, which);
            }

            @Override
            public void dismiss() {
                if (allowClose) {
                    super.dismiss();
                }
            }

        };

//        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
//                getString(R.string.cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        if (which == DialogInterface.BUTTON_NEGATIVE) {
////                            dialog.cancel();
//
//                        }
//                    }
//                });
//
//        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                "OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//
//                        DatePicker datePicker = datePickerDialog
//                                .getDatePicker();
//
//                        if (datePicker.getYear() < minYear || datePicker.getMonth() < minMonth && datePicker.getYear() == minYear ||
//                                datePicker.getDayOfMonth() < minDay && datePicker.getYear() == minYear && datePicker.getMonth() == minMonth) {
//
//                            datePicker.updateDate(minYear, minMonth, minDay);
//                            etDOB.setText(datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
////                                if (which == DialogInterface.BUTTON_POSITIVE) {
////                                    dialog.cancel();
////
////                                }
//                        } else {
//                            Toast.makeText(AccountSettingActivity.this, R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                    }
//                });


        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }

//    private void updateLabel() {
//        String myFormat = "dd/MM/yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        etDOB.setText(sdf.format(myCalendar.getTime()));
//    }

    @OnClick(R.id.tvSave)
    public void tvSaveClick() {
        saveData();
    }


    public void saveData() {
        PostApi postApi = new PostApi(this, "update_customer", this);
        try {

            customer.firstName = etFirstName.getText().toString();
            customer.lastName = etLastName.getText().toString();
            customer.dob = etDOB.getText().toString();

            String data = new Gson().toJson(customer);
            JSONObject jsonObject = new JSONObject(data);

            String id = getPreferences().getString(RequestParamUtils.ID, "");
            String phone = etMobileNumber.getText().toString();
            jsonObject.put("user_id", id);
            jsonObject.put("mobile", phone);

            if (ivRightMale.getVisibility() == View.VISIBLE) {
                jsonObject.put("gender", "Male");
            } else if (ivRightFemale.getVisibility() == View.VISIBLE) {
                jsonObject.put("gender", "Female");
            }

            postApi.callPostApi(new URLS().UPDATE_CUSTOMER, jsonObject.toString());
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public void onResponse(final String response, String methodName) {

        if (methodName.equals("update_customer")) {

            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        customer = new Gson().fromJson(
                                response, new TypeToken<Customer>() {
                                }.getType());

                        SharedPreferences.Editor pre = getPreferences().edit();
                        pre.putString(RequestParamUtils.CUSTOMER, response);
                        pre.commit();
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