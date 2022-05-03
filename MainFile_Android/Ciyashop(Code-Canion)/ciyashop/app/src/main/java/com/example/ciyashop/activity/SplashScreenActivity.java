package com.example.ciyashop.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.example.ciyashop.R;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.Utils;
import com.example.ciyashop.utils.apicall.PostApi;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.tvSplashText)
    TextViewRegular tvSplashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        tvSplashText.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        Constant.DEVICE_TOKEN = refreshedToken;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int systemtime = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0);
            Log.e("System time is ", systemtime + " ");
        } else {
            int systemtime = Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME, 0);
            Log.e("System time is ", systemtime + " ");
        }
        if (Constant.DEVICE_TOKEN != null && !Constant.DEVICE_TOKEN.equals("")) {
            if (getPreferences().getString(RequestParamUtils.DEVICE_TOKEN, "").equals("") ||
                    !getPreferences().getString(RequestParamUtils.DEVICE_TOKEN, "").equals(Constant.DEVICE_TOKEN)) {
                addDeviceToken();

            }
        }
        Log.e("token", "Refreshed token: " + refreshedToken);
        if (mayRequestPermission()) {
            setData();
        }

        Utils.printHashKey(this);


    }

    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1212);

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1212) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setData();
            } else {
                Snackbar.make(findViewById(R.id.crMain), "Permission must be need", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }).show();
            }
        }
    }


    public void setData() {
        buildGoogleApiClient();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    public void addDeviceToken() {

        PostApi postApi = new PostApi(SplashScreenActivity.this, "addDeviceToken", this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RequestParamUtils.DEVICE_TOKEN, Constant.DEVICE_TOKEN);
            jsonObject.put(RequestParamUtils.DEVICE_TYPE, "android");
            postApi.setisDialogShow(false);
            postApi.callPostApi(new URLS().ADDNOTIFICATION, jsonObject.toString());
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }
    }

    @Override
    public void onResponse(String response, String methodName) {
        if (methodName.equals("addDeviceToken")) {
            if (response != null && response.length() > 0) {
                try {
                    Log.e("Response is ", response);
                    getPreferences().edit().putString(RequestParamUtils.DEVICE_TOKEN, Constant.DEVICE_TOKEN).commit();
                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        }
    }

}
