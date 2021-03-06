package com.example.ciyashop.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewMedium;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.R;
import com.example.ciyashop.utils.Constant;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductQuickDetailActivity extends BaseActivity   {

    @BindView(R.id.tvSubTitle)
    TextViewMedium tvSubTitle;

    @BindView(R.id.ivProduct)
    ImageView ivProduct;

    @BindView(R.id.tvProductName)
    TextViewBold tvProductName;

    @BindView(R.id.tvDescription)
    HtmlTextView tvDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_quick_detail);
        ButterKnife.bind(this);
        setToolbarTheme();
        hideSearchNotification();
        setScreenLayoutDirection();

        String description = getIntent().getExtras().getString("description");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tvDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            tvDescription.setText(Html.fromHtml(description));
//        }

        tvDescription.setHtml(description,
                new HtmlHttpImageGetter(tvDescription));

        String subTitle = getIntent().getExtras().getString("title");
        String productImage = getIntent().getExtras().getString("image");
        String productName = getIntent().getExtras().getString("name");

        settvTitle(subTitle);
        tvSubTitle.setText(subTitle);
        tvProductName.setText(productName);
        tvProductName.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));

        if (productImage.length() > 0) {
            ivProduct.setVisibility(View.VISIBLE);
            Picasso.with(this).load(productImage).into(ivProduct);
        } else {
            ivProduct.setVisibility(View.INVISIBLE);
        }

        showBackButton();
    }




}
