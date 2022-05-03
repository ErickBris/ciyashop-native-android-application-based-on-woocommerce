package com.example.ciyashop.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.example.ciyashop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

public class TermsAndPrivacyActivity extends BaseActivity implements OnResponseListner {

    String data;

    @BindView(R.id.tvContentDesc)
    TextViewLight tvContentDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_privacy);
        ButterKnife.bind(this);
        showBackButton();
        setToolbarTheme();
        hideSearchNotification();
        data = getIntent().getStringExtra("Data");

        if (data.equals("privacy_policy")) {
            settvTitle(getResources().getString(R.string.privacy_policy));
        } else if (data.equals("terms_of_use")) {
            settvTitle(getResources().getString(R.string.terms_condition));
        }

        getPages();
    }

    public void getPages() {
        PostApi postApi = new PostApi(this, "static_page", this);
        JSONObject object = new JSONObject();
        try {

            object.put("page", data);
            postApi.callPostApi(new URLS().STATIC_PAGES, object.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName) {

        if (methodName.equals("static_page")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    //set call here
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        String content = jsonObj.getString("data");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tvContentDesc.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tvContentDesc.setText(Html.fromHtml(content));
                        }
                    } else {
                        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
