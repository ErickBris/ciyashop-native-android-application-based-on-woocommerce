package com.example.ciyashop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.CartAdapter;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Cart;
import com.example.ciyashop.model.CategoryList;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {

    @BindView(R.id.rvCart)
    RecyclerView rvCart;

    @BindView(R.id.llMain)
    LinearLayout llMain;

    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;

    @BindView(R.id.tvEmptyTitle)
    TextViewBold tvEmptyTitle;

    @BindView(R.id.tvTotalItem)
    TextViewLight tvTotalItem;

    @BindView(R.id.tvAmount)
    TextViewRegular tvAmount;

    @BindView(R.id.tvTotalAmount)
    TextViewRegular tvTotalAmount;

    @BindView(R.id.tvViewAmount)
    TextViewRegular tvViewAmount;

    @BindView(R.id.tvContinue)
    TextViewRegular tvContinue;

    @BindView(R.id.llViewDetail)
    LinearLayout llViewDetail;

    @BindView(R.id.tvContinueShopping)
    TextViewRegular tvContinueShopping;

    @BindView(R.id.ivGo)
    ImageView ivGo;

    private CartAdapter cartAdapter;
    private DatabaseHelper databaseHelper;
    private Bundle bundle;
    private String id;
    private int buyNow;
    private String checkOutUrl, THANKYOU, HOMEURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        setToolbarTheme();
        setThemeColor();
        setScreenLayoutDirection();
        getIntentData();
        setCartAdapter();
        settvTitle(getString(R.string.cart));
        showBackButton();
        hideSearchNotification();
        getCartData();
    }

    public void setThemeColor() {
        llViewDetail.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvContinue.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        setEmptyColor();
    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(RequestParamUtils.ID);
            buyNow = bundle.getInt("buynow");
        }
    }

    public void setCartAdapter() {
        cartAdapter = new CartAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(mLayoutManager);
        rvCart.setAdapter(cartAdapter);
        cartAdapter.isFromBuyNow(buyNow);
        rvCart.setNestedScrollingEnabled(false);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (cartAdapter != null) {
            cartAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (cartAdapter != null) {
            cartAdapter.restoreStates(savedInstanceState);
        }
    }

    /**
     * {@link OnItemClickListner#onItemClick(int position, String value, int OuterPos)) onClick} may be used on the
     * method.
     *
     * @see OnItemClickListner
     */
    @Override
    public void onItemClick(int position, String value, int OuterPos) {
        if (value.equals("delete")) {
            if (cartAdapter.getList().size() == 0) {
                isEmptlyLayout(true);
            } else {
                setTotalCount();
            }
        } else if (value.equals("increment") || value.equals("decrement")) {
            setTotalCount();
        }else if(value.equals("detail")) {
            databaseHelper.deleteFromBuyNow(OuterPos+ "");
        }

    }

    public void isEmptlyLayout(boolean isEMpty) {
        if (isEMpty) {
            llEmpty.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
            tvEmptyTitle.setText(R.string.cart_empty);
        } else {
            llEmpty.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
        }
    }
    public void getCartData() {
        List<Cart> cartList = databaseHelper.getFromCart(buyNow);
        if (cartList.size() > 0) {
            for (int i = 0; i < cartList.size(); i++) {
                String product = cartList.get(i).getProduct();
                try {
                    CategoryList categoryListRider = new Gson().fromJson(product, new TypeToken<CategoryList>() {
                    }.getType());
                    cartList.get(i).setCategoryList(categoryListRider);
                } catch (Exception e) {
                    Log.e("Gson Exception", "in Recent Product Get" + e.getMessage());
                }
            }
            cartAdapter.addAll(cartList);
            setTotalCount();

        } else {
            isEmptlyLayout(true);
        }
    }

    public void setTotalCount() {
        tvTotalItem.setText(cartAdapter.getList().size() + getString(R.string.items));
        for (int i = 0; i < cartAdapter.getList().size(); i++) {
            String price = Html.fromHtml(cartAdapter.getList().get(i).getCategoryList().priceHtml).toString();
            if (Constant.CURRENCYSYMBOL == null && !price.equals("")) {
                Constant.CURRENCYSYMBOL = price.charAt(i) + "";
                break;
            }
        }
        float amount = 0;
        for (int i = 0; i < cartAdapter.getList().size(); i++) {
            amount = amount + (Float.parseFloat(cartAdapter.getList().get(i).getCategoryList().price) * cartAdapter.getList().get(i).getQuantity());
        }
        if (Constant.CURRENCYSYMBOLPOSTION.equals("left")) {
            tvAmount.setText(Constant.CURRENCYSYMBOL + Constant.setDecimal((double) amount) + "");
            tvTotalAmount.setText(Constant.CURRENCYSYMBOL + Constant.setDecimal((double) amount) + "");
            tvViewAmount.setText(Constant.CURRENCYSYMBOL + Constant.setDecimal((double) amount) + "");
        } else if (Constant.CURRENCYSYMBOLPOSTION.equals("right")) {
            tvAmount.setText(Constant.setDecimal((double) amount) + Constant.CURRENCYSYMBOL + "");
            tvTotalAmount.setText(Constant.setDecimal((double) amount) + Constant.CURRENCYSYMBOL + "");
            tvViewAmount.setText(Constant.setDecimal((double) amount) + Constant.CURRENCYSYMBOL + "");
        } else if (Constant.CURRENCYSYMBOLPOSTION.equals("left_space")) {
            tvAmount.setText(Constant.CURRENCYSYMBOL + " " + Constant.setDecimal((double) amount) + "");
            tvTotalAmount.setText(Constant.CURRENCYSYMBOL + " " + Constant.setDecimal((double) amount) + "");
            tvViewAmount.setText(Constant.CURRENCYSYMBOL + " " + Constant.setDecimal((double) amount) + "");
        } else if (Constant.CURRENCYSYMBOLPOSTION.equals("right_space")) {
            tvAmount.setText(Constant.setDecimal((double) amount) + " " + Constant.CURRENCYSYMBOL + "");
            tvTotalAmount.setText(Constant.setDecimal((double) amount) + " " + Constant.CURRENCYSYMBOL + "");
            tvViewAmount.setText(Constant.setDecimal((double) amount) + " " + Constant.CURRENCYSYMBOL + "");
        }
    }


    @OnClick(R.id.tvContinue)
    public void tvContinueClick() {
     addToCartCheckOut();
    }


    public JSONArray getCartDataForAPI() {
        List<Cart> cartList = databaseHelper.getFromCart(buyNow);
        if (cartList.size() > 0) {

            try {

                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < cartList.size(); i++) {
//                    String product = cartList.get(i).getProduct();

                    JSONObject object = new JSONObject();

                    object.put("product_id", cartList.get(i).getProductid() + "");
                    object.put("quantity", cartList.get(i).getQuantity() + "");

                    if (cartList.get(i).getVariation() != null) {
                        JSONObject ob1 = new JSONObject(cartList.get(i).getVariation());

                        object.put("variation", ob1);
                    }


                    object.put("variation_id", cartList.get(i).getVariationid() + "");

                    jsonArray.put(object);
                }
                return jsonArray;

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
        return null;
    }


    public void addToCartCheckOut() {

        PostApi postApi = new PostApi(this, "add_to_cart", this);
        try {
            JSONObject jsonObject = new JSONObject();
            String customerId = getPreferences().getString(RequestParamUtils.ID, "");
            jsonObject.put("user_id", customerId);
            jsonObject.put("cart_items", getCartDataForAPI());
            jsonObject.put("os", "android");
            jsonObject.put("device_token", Constant.DEVICE_TOKEN);
            postApi.callPostApi(new URLS().ADD_TO_CART + getPreferences().getString(RequestParamUtils.CurrencyText, ""), jsonObject.toString());

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

    }


    @Override
    public void onResponse(final String response, String methodName) {

        if (methodName.equals("add_to_cart")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                Log.e("Response " + methodName, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        THANKYOU = jsonObj.getString(RequestParamUtils.THANKYOU);
                        checkOutUrl = jsonObj.getString(RequestParamUtils.CHECKOUT_URL);
                        HOMEURL = jsonObj.getString(RequestParamUtils.HOME_URL);
                        Intent intent = new Intent(this, WebviewActivity.class);
                        intent.putExtra("buynow", buyNow);
                        intent.putExtra(RequestParamUtils.THANKYOU, THANKYOU);
                        intent.putExtra(RequestParamUtils.CHECKOUT_URL, checkOutUrl);
                        intent.putExtra(RequestParamUtils.HOME_URL, jsonObj.getString(RequestParamUtils.HOME_URL));
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.something_went_wrong_try_after_somtime, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        databaseHelper.deleteFromBuyNow(id);
        super.onBackPressed();
    }


    @OnClick(R.id.tvContinueShopping)
    public void tvContinueShoppingClick() {
        Intent i = new Intent(CartActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Cart Activity", "On Restart Called");
        getCartData();
    }
}
