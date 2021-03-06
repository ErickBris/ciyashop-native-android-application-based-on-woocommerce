package com.example.ciyashop.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.DownloadAdapter;
import com.example.ciyashop.adapter.MyOrderAdapter;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Download;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.GetApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {

    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;

    @BindView(R.id.rvDownload)
    RecyclerView rvDownload;

    @BindView(R.id.tvEmptyTitle)
    TextViewBold tvEmptyTitle;

    @BindView(R.id.tvEmptyDesc)
    TextViewLight tvEmptyDesc;

    @BindView(R.id.tvContinueShopping)
    TextViewRegular tvContinueShopping;

    DownloadAdapter downloadAdapter;

    List<Download> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        setDownloadAdapter();
        getDownloadProducts();
        settvTitle(getString(R.string.download));
        setToolbarTheme();
        setScreenLayoutDirection();
        showBackButton();
        hideSearchNotification();
        setThemeColor();
    }

    void setDownloadAdapter() {
        downloadAdapter = new DownloadAdapter(this, this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDownload.setLayoutManager(mLayoutManager);
        rvDownload.setAdapter(downloadAdapter);
        rvDownload.setNestedScrollingEnabled(false);
    }

    public void setThemeColor() {
        TextViewRegular tvContinueShopping = findViewById(R.id.tvContinueShopping);
        ImageView ivGo = findViewById(R.id.ivGo);
        tvContinueShopping.setTextColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(5, Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        tvContinueShopping.setBackground(gradientDrawable);
        ivGo.setColorFilter(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));

    }

    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }

    void getDownloadProducts() {
        GetApi getApi = new GetApi(this, "getDownloads", this);
        String customerId = getPreferences().getString(RequestParamUtils.ID, "");
//        showProgress("");
        getApi.callGetApi(new URLS().WOO_MAIN_URL + new URLS().WOO_CUSTOMERS + "/" + customerId + "/" + new URLS().WOO_DOWNLOADS_URL);
    }

    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();
        if (response == null && response.length() < 0) {
            return;
        } else if (methodName.endsWith("getDownloads")) {
            Log.e("getDownload", response);
            try {

                JSONArray jsonArray = new JSONArray(response);
                list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String jsonResponse = jsonArray.get(i).toString();
                    Download downloadListRider = new Gson().fromJson(
                            jsonResponse, new TypeToken<Download>() {
                            }.getType());
                    list.add(downloadListRider);
                }
                downloadAdapter.addAll(list);
                if (list.size() == 0) {
                    showEmptyLayout();
                } else {
                    llEmpty.setVisibility(View.GONE);
                }
                dismissProgress();

            } catch (Exception e) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("message").equals("No product found")) {
//                        setNoItemFound = true;
                        if (rvDownload.getAdapter().getItemCount() == 0) {
                            showEmptyLayout();
                        }
                    }
                } catch (JSONException e1) {
                    Log.e("noproductJSONException", e1.getMessage());
                }
                Log.e(methodName + "Gson Exception is ", e.getMessage());
                dismissProgress();
            }

        }
    }

    public void showEmptyLayout() {
        llEmpty.setVisibility(View.VISIBLE);
        tvEmptyTitle.setText(getString(R.string.no_product_found));
        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDownloadProducts();
    }
}
