package com.example.ciyashop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.HomeTopCategoryAdapter;
import com.example.ciyashop.adapter.MyPointSAdapter;
import com.example.ciyashop.customview.CustomLinearLayoutManager;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.javaclasses.FilterSelectedList;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.model.MyPoint;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPointActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {

    @BindView(R.id.rvMyPoints)
    RecyclerView rvMyPoints;

    @BindView(R.id.tvMyPoint)
    TextViewRegular tvMyPoint;

    @BindView(R.id.llMyPoint)
    LinearLayout llMyPoint;

    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;

    @BindView(R.id.tvEmptyTitle)
    TextViewBold tvEmptyTitle;

    @BindView(R.id.tvContinueShopping)
    TextViewRegular tvContinueShopping;

    @BindView(R.id.tvEmptyDesc)
    TextViewLight tvEmptyDesc;

    private MyPointSAdapter myPointSAdapter;
    private Bundle bundle;
    private String userId;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    Boolean setNoItemFound = false;
    private int page = 1;
    CustomLinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point);
        ButterKnife.bind(this);
        settvTitle(getString(R.string.my_point));
        hideSearchNotification();
        showBackButton();
        setToolbarTheme();
        setPointAdapter();
        getIntentData();
        getMyPoint(page, true);
    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString(RequestParamUtils.USER_ID, "");
        }
    }

    public void setPointAdapter() {
        myPointSAdapter = new MyPointSAdapter(this, this);
        mLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMyPoints.setLayoutManager(mLayoutManager);
        rvMyPoints.setAdapter(myPointSAdapter);
        rvMyPoints.setNestedScrollingEnabled(false);
        rvMyPoints.setHasFixedSize(true);
        rvMyPoints.setItemViewCacheSize(20);
        rvMyPoints.setDrawingCacheEnabled(true);
        rvMyPoints.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvMyPoints.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (setNoItemFound != true) {
                                loading = false;
                                page = page + 1;
                                Log.e("End ", "Last Item Wow  and page no:- " + page);
                                getMyPoint(page, true);
                                //Do pagination.. i.e. fetch new data
                            }
                        }
                    }
                }
            }
        });
    }

    public void setadapter() {

    }


    public void getMyPoint(int page, boolean isDialogShow) {

        PostApi postApi = new PostApi(this, "getMyPoint", this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(RequestParamUtils.USER_ID, userId);
            jsonObject.put(RequestParamUtils.PAGE, page);
            postApi.setisDialogShow(true);
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }
        postApi.setisDialogShow(isDialogShow);
        postApi.callPostApi(new URLS().REWARDSPOINT, jsonObject.toString());
    }

    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }


    @Override
    public void onResponse(final String response, String methodName) {
        dismissProgress();

        if (methodName.equals("getMyPoint")) {
            if (response != null && response.length() > 0) {
                try {
                    MyPoint myPointRider = new Gson().fromJson(
                            response, new TypeToken<MyPoint>() {
                            }.getType());
                    showData();
                    if (myPointRider.status.equals("success")) {
                        loading = true;
                        tvMyPoint.setText(myPointRider.data.pointsBalance + "");
                        myPointSAdapter.addAll(myPointRider.data.events);
                        if (myPointRider.data.events.size() > 0) {

                            if (Integer.parseInt(myPointRider.data.totalRows) > myPointSAdapter.getList().size() &&
                                    myPointSAdapter.getList().size() < 20) {
                                page = page + 1;
                                getMyPoint(page, false);
                            }
                        } else {
                            setNoItemFound = true;
                        }
                    } else {
                        setNoItemFound = true;
                    }

//                    if (myPointSAdapter.getList().size() == 0) {
//                        showNoData();
//                    } else {
//                        showData();
//                    }

                } catch (Exception e) {
                    dismissProgress();
                    showNoData();
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        }
    }


    public void showNoData() {
        llEmpty.setVisibility(View.VISIBLE);
        llMyPoint.setVisibility(View.GONE);
        tvEmptyTitle.setText(R.string.no_points_earned);
        tvEmptyDesc.setText(R.string.purchase_product_and_erned_pointa);
        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(MyPointActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    public void showData() {
        llEmpty.setVisibility(View.GONE);
        llMyPoint.setVisibility(View.VISIBLE);
    }

}
