package com.example.ciyashop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ciyashop.R;
import com.example.ciyashop.adapter.OrderDetailAdapter;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Orders;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {

    @BindView(R.id.tvOrderId)
    TextViewRegular tvOrderId;

    @BindView(R.id.tvOrderDateAndStatus)
    TextViewRegular tvOrderDateAndStatus;

    @BindView(R.id.tvTrackMessage1)
    TextViewRegular tvTrackMessage1;

    @BindView(R.id.tvTrackMessage2)
    TextViewRegular tvTrackMessage2;

    @BindView(R.id.tvTrackID)
    TextViewRegular tvTrackID;

//    @BindView(R.id.tvProductName)
//    TextViewLight tvProductName;

//    @BindView(R.id.tvProductPrice)
//    TextViewLight tvProductPrice;

    @BindView(R.id.tvSubTotal)
    TextViewBold tvSubTotal;

    @BindView(R.id.tvShippingCharges)
    TextViewBold tvShippingCharges;

    @BindView(R.id.tvPaymentMethodTitle)
    TextViewBold tvPaymentMethodTitle;

    @BindView(R.id.tvTotal)
    TextViewBold tvTotal;

    @BindView(R.id.tvEmail)
    TextViewLight tvEmail;

    @BindView(R.id.tvPhone)
    TextViewLight tvPhone;

    @BindView(R.id.tvBillingCompanyName)
    TextViewLight tvBillingCompanyName;

    @BindView(R.id.tvBillingName)
    TextViewLight tvBillingName;

    @BindView(R.id.tvBillingAddress1)
    TextViewLight tvBillingAddress1;

    @BindView(R.id.tvBillingAddress2)
    TextViewLight tvBillingAddress2;

    @BindView(R.id.tvBillingCityPin)
    TextViewLight tvBillingCityPin;

    @BindView(R.id.tvBillingCountryState)
    TextViewLight tvBillingCountryState;

    @BindView(R.id.tvShippingCompanyName)
    TextViewLight tvShippingCompanyName;

    @BindView(R.id.tvShippingName)
    TextViewLight tvShippingName;

    @BindView(R.id.tvShippingAddress1)
    TextViewLight tvShippingAddress1;

    @BindView(R.id.tvShippingAddress2)
    TextViewLight tvShippingAddress2;

    @BindView(R.id.tvShippingCityPin)
    TextViewLight tvShippingCityPin;

    @BindView(R.id.tvShippingCountryState)
    TextViewLight tvShippingCountryState;

    @BindView(R.id.tvCancelOrder)
    TextViewBold tvCancelOrder;

    private Orders orderData = Constant.ORDERDETAIL;
    private OrderDetailAdapter orderDetailAdapter;

    private List<Orders.OrderTrackingData> list = new ArrayList<>();
    private Activity activity;

    String Trackurl;

    @BindView(R.id.rvOrderedContent)
    RecyclerView rvOrderedContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.my_orders));
        showBackButton();
        setToolbarTheme();
        setColorTheme();
        setData();
    }

    public void setColorTheme() {
        tvOrderId.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvTrackID.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvCancelOrder.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }

    public void setData() {
        String orderId = "#" + orderData.id;
        String upperString = orderData.status.substring(0, 1).toUpperCase() + orderData.status.substring(1);

        if (Constant.IS_ORDER_TRACKING_ACTIVE && orderData.orderTrackingData.size() != 0) {
            for (int i = 0; i < orderData.orderTrackingData.size(); i++) {
                tvTrackMessage1.setVisibility(View.VISIBLE);
                tvTrackMessage2.setVisibility(View.VISIBLE);
                tvTrackMessage1.setText(orderData.orderTrackingData.get(i).trackmessage1);
                tvTrackMessage2.setText("Tracking number is ");
                Trackurl = orderData.orderTrackingData.get(i).ordertrackinglink;
                tvTrackID.setText(orderData.orderTrackingData.get(i).trackmessage2);

                if (orderData.orderTrackingData.get(i).usetrackbutton == true) {
                    tvTrackID.setClickable(true);

                } else {
                    tvTrackID.setClickable(false);
                }
            }


        } else {
            tvTrackMessage1.setVisibility(View.GONE);
            tvTrackMessage2.setVisibility(View.GONE);
        }
//        Locale uk = new Locale("en", "GB");
        Currency pound = Currency.getInstance(orderData.currency);
        String currencySymbol = pound.getSymbol();

        tvOrderId.setText(orderId);

        tvOrderDateAndStatus.setText(getString(R.string.order)+" " + orderId + " "+getString(R.string.was_place_on)+" " + Constant.setDate(orderData.dateCreated) + " "+getString(R.string.and_currently)+" " + upperString);

        setProductList(currencySymbol);

        float subtotalPrice = 0;
        for (int i = 0; i < orderData.lineItems.size(); i++) {
            subtotalPrice = subtotalPrice + Float.parseFloat(orderData.lineItems.get(i).total);
        }

        tvSubTotal.setText(currencySymbol + " " + subtotalPrice);
        tvShippingCharges.setText(currencySymbol + " " + orderData.shippingTotal);
        tvTotal.setText(currencySymbol + " " + orderData.total);

        tvPaymentMethodTitle.setText(orderData.paymentMethodTitle + "");

        tvEmail.setText(orderData.billing.email + "");
        tvPhone.setText(orderData.billing.phone + "");

        tvBillingCompanyName.setText(orderData.billing.company + "");
        tvBillingName.setText(orderData.billing.firstName + " " + orderData.billing.lastName);
        tvBillingAddress1.setText(orderData.billing.address1 + "");
        tvBillingAddress2.setText(orderData.billing.address2 + "");
        tvBillingCityPin.setText(orderData.billing.city + " " + orderData.billing.postcode);

        Locale lcountry = new Locale("", orderData.billing.country);
        String billingCountry = lcountry.getDisplayCountry();
        tvBillingCountryState.setText(orderData.billing.state + ", " + billingCountry);

        tvShippingCompanyName.setText(orderData.shipping.company + "");
        tvShippingName.setText(orderData.shipping.firstName + " " + orderData.shipping.lastName);
        tvShippingAddress1.setText(orderData.shipping.address1 + "");
        tvShippingAddress2.setText(orderData.shipping.address2 + "");
        tvShippingCityPin.setText(orderData.shipping.city + " " + orderData.shipping.postcode);

        Locale lcountryShip = new Locale("", orderData.shipping.country);
        String shippingCountryShip = lcountryShip.getDisplayCountry();
        tvShippingCountryState.setText(orderData.shipping.state + ", " + shippingCountryShip);

        if (orderData.status.toLowerCase().equals("on-hold") || orderData.status.toLowerCase().equals("pending")) {
            tvCancelOrder.setClickable(true);
            tvCancelOrder.setAlpha((float) 1);
        } else {
            tvCancelOrder.setClickable(false);
            tvCancelOrder.setAlpha((float) 0.3);
        }
    }

    @OnClick(R.id.tvTrackID)
    public void tvTrackIdClick() {
        if (!Trackurl.startsWith("http://") && !Trackurl.startsWith("https://")) {
            Trackurl = "http://" + Trackurl;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Trackurl));
        startActivity(browserIntent);

    }

    public void setProductList(String currencySymbol) {
        orderDetailAdapter = new OrderDetailAdapter(this, this, currencySymbol);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvOrderedContent.setLayoutManager(mLayoutManager);
        rvOrderedContent.setAdapter(orderDetailAdapter);
        rvOrderedContent.setNestedScrollingEnabled(false);
        orderDetailAdapter.addAll(orderData.lineItems);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }

    @OnClick(R.id.tvCancelOrder)
    public void setTvCancelOrderClick() {
        cancelOrder();
    }

    public void cancelOrder() {
        PostApi postApi = new PostApi(this, "cancel_order", this);
        JSONObject object = new JSONObject();
        try {
            object.put("order", orderData.id + "");
            postApi.callPostApi(new URLS().CANCEL_ORDER, object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName) {

        if (methodName.equals("cancel_order")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    //set call here
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("result");
                    if (status.equals("success")) {
                        Toast.makeText(this, R.string.order_is_cancelled, Toast.LENGTH_SHORT).show();
                        tvCancelOrder.setClickable(false);
                        tvCancelOrder.setAlpha((float) 0.3);
                        finish();
                        //TODO:code here for cancelled order
                    } else {
                        Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();
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