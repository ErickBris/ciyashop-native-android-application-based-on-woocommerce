package com.example.ciyashop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.SellerReviewAdapter;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.SellerData;
import com.example.ciyashop.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellerReviewActivity extends BaseActivity implements OnItemClickListner {

    @BindView(R.id.rvReview)
    RecyclerView rvReview;

    private SellerReviewAdapter sellerReviewAdapter;

    List<SellerData.SellerInfo.ReviewList> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_review);
        ButterKnife.bind(this);
        settvTitle("Vendor Review");
        showBackButton();
        setToolbarTheme();

        String listData = getIntent().getExtras().getString("sellerInfo");

        SellerData sellerDataRider = new Gson().fromJson(
                listData, new TypeToken<SellerData>() {
                }.getType());

        list.addAll(sellerDataRider.sellerInfo.reviewList);

        setReviewData();
    }

    public void setReviewData() {
        sellerReviewAdapter = new SellerReviewAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReview.setLayoutManager(mLayoutManager);
        rvReview.setAdapter(sellerReviewAdapter);
        rvReview.setNestedScrollingEnabled(false);
        sellerReviewAdapter.addAll(list);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }
}
