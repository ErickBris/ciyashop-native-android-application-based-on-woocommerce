package com.example.ciyashop.activity;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoPageDetailActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.tvDescription)
    TextViewLight tvDescription;

    private String pageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page_detail);
        ButterKnife.bind(this);
        pageID = getIntent().getExtras().getString(RequestParamUtils.ID);
        settvTitle(getIntent().getExtras().getString("title"));
        showBackButton();
        getPageData();
        setToolbarTheme();
    }

    public void getPageData() {
        PostApi postApi = new PostApi(this, "info_pages", this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page_id", pageID);
            postApi.callPostApi(new URLS().INFO_PAGES, jsonObject.toString());
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }
    }

    @Override
    public void onResponse(String response, String methodName) {

        if (methodName.equals("info_pages")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tvDescription.setText(Html.fromHtml(jsonObject.getString("data"), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tvDescription.setText(Html.fromHtml(jsonObject.getString("data")));
                        }
                    }
                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        }
    }

}
