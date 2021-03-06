package com.example.ciyashop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.GroupProductAdapter;
import com.example.ciyashop.adapter.ProductColorAdapter;
import com.example.ciyashop.adapter.ProductImageViewPagerAdapter;
import com.example.ciyashop.adapter.ProductSimpleAdapter;
import com.example.ciyashop.adapter.ProductVariationAdapter;
import com.example.ciyashop.adapter.ReviewAdapter;
import com.example.ciyashop.customview.MaterialRatingBar;
import com.example.ciyashop.customview.like.animation.SparkButton;
import com.example.ciyashop.customview.progressbar.RoundCornerProgressBar;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.javaclasses.CheckIsVariationAvailable;
import com.example.ciyashop.javaclasses.CheckSimpleSelector;
import com.example.ciyashop.model.Cart;
import com.example.ciyashop.model.CategoryList;
import com.example.ciyashop.model.ProductReview;
import com.example.ciyashop.model.Variation;
import com.example.ciyashop.model.WishList;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.CustomToast;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.GetApi;
import com.example.ciyashop.utils.apicall.OneLeggedApi10;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.vpProductImages)
    ViewPager vpProductImages;

    @BindView(R.id.rvColor)
    RecyclerView rvColor;

    @BindView(R.id.rvReview)
    RecyclerView rvReview;

    @BindView(R.id.rvGroupProduct)
    RecyclerView rvGroupProduct;

    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;

    @BindView(R.id.llColor)
    LinearLayout llColor;

    @BindView(R.id.tvColor)
    TextViewRegular tvColor;

    @BindView(R.id.tvProductName)
    TextViewBold tvProductName;

    @BindView(R.id.rattingFive)
    RoundCornerProgressBar rattingFive;

    @BindView(R.id.rattingFour)
    RoundCornerProgressBar rattingFour;

    @BindView(R.id.rattingThree)
    RoundCornerProgressBar rattingThree;

    @BindView(R.id.rattingTwo)
    RoundCornerProgressBar rattingTwo;

    @BindView(R.id.rattingOne)
    RoundCornerProgressBar rattingOne;

    @BindView(R.id.tvAverageRatting)
    TextViewRegular tvAverageRatting;

    private static TextViewRegular tvPrice;

    private static TextViewRegular tvPrice1;

    @BindView(R.id.tvAvailibility)
    TextViewRegular tvAvailibility;

    @BindView(R.id.tvRatting)
    TextViewRegular tvRatting;

    @BindView(R.id.llIsSeller)
    LinearLayout llIsSeller;

    @BindView(R.id.tvSellerName)
    TextViewLight tvSellerName;

    @BindView(R.id.tvSellerContent)
    TextViewLight tvSellerContent;

    @BindView(R.id.tvSellerMore)
    TextViewLight tvSellerMore;

    @BindView(R.id.llQuickOverView)
    LinearLayout llQuickOverView;

    @BindView(R.id.tvDescription)
    TextViewLight tvDescription;

    @BindView(R.id.llProductDescription)
    LinearLayout llProductDescription;

    @BindView(R.id.tvProductDescription)
    TextViewLight tvProductDescription;

    @BindView(R.id.llReview)
    LinearLayout llReview;

    @BindView(R.id.tvContactSeller)
    TextViewRegular tvContactSeller;

    @BindView(R.id.tvFive)
    TextViewRegular tvFive;

    @BindView(R.id.tvFour)
    TextViewRegular tvFour;

    @BindView(R.id.tvThree)
    TextViewRegular tvThree;

    @BindView(R.id.tvTwo)
    TextViewRegular tvTwo;

    @BindView(R.id.tvOne)
    TextViewRegular tvOne;

    RecyclerView rvProductVariation;

    @BindView(R.id.tvCart)
    TextViewBold tvCart;

    @BindView(R.id.tvBuyNow)
    TextViewBold tvBuyNow;

    @BindView(R.id.ivWishList)
    SparkButton ivWishList;

    @BindView(R.id.tvMoreQuickOverview)
    TextViewLight tvMoreQuickOverview;

    @BindView(R.id.tvMoreDetail)
    TextViewLight tvMoreDetail;

    @BindView(R.id.llratting)
    LinearLayout llratting;

    @BindView(R.id.ivQuickOverViewMore)
    ImageView ivQuickOverViewMore;

    @BindView(R.id.ivDetailMore)
    ImageView ivDetailMore;

    @BindView(R.id.ivMoreSeller)
    ImageView ivMoreSeller;

    @BindView(R.id.tvViewStore)
    TextViewRegular tvViewStore;

    @BindView(R.id.tvRateReview)
    TextViewRegular tvRateReview;

    @BindView(R.id.tvReward)
    TextViewLight tvReward;

    @BindView(R.id.ratingBar)
    MaterialRatingBar ratingBar;


    private boolean isDialogOpen = false;

    private TextView[] dots;
    private int[] layouts;
    private ProductImageViewPagerAdapter productImageViewPagerAdapter;
    private ProductSimpleAdapter productSimpleAdapter;
    private int currentPosition;
    private ProductColorAdapter productColorAdapter;
    private ReviewAdapter reviewAdapter;
    private GroupProductAdapter groupProductAdapter;
    private ProductVariationAdapter productVariationAdapter;
    private CategoryList categoryList = Constant.CATEGORYDETAIL;
    private List<Variation> variationList;
    private int page = 1;
    public static HashMap<Integer, String> combination = new HashMap<>();
    private DatabaseHelper databaseHelper;
    private CustomToast toast;
    AlertDialog alertDialog;
    private float fiveRate, fourRate, threeRate, twoRate, oneRate;
    private float avgRatting;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        ivWishList.setActivetint(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivWishList.setColors(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), Color.parseColor(getPreferences().getString(Constant.APP_TRANSPARENT, Constant.PRIMARY_COLOR)));
        setScreenLayoutDirection();
        setToolbarTheme();
        categoryList = Constant.CATEGORYDETAIL;
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice1 = findViewById(R.id.tvPrice1);
        databaseHelper = new DatabaseHelper(this);
        String product = new Gson().toJson(categoryList);
        databaseHelper.addTorecentView(product, categoryList.id + "");
        toast = new CustomToast(this);
        if (Constant.IS_WISH_LIST_ACTIVE) {
            ivWishList.setVisibility(View.VISIBLE);
            if (databaseHelper.getWishlistProduct(categoryList.id + "")) {
                ivWishList.setChecked(true);
            } else {
                ivWishList.setChecked(false);
            }
        } else {
            ivWishList.setVisibility(View.GONE);
        }
        setData();
        setColorTheme();
    }

    private void setColorTheme() {
        tvMoreQuickOverview.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvMoreDetail.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvFive.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvFour.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvThree.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvTwo.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvOne.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvBuyNow.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        GradientDrawable gd = (GradientDrawable) llratting.getBackground();
        gd.setStroke(5, Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivDetailMore.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivQuickOverViewMore.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvSellerName.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvSellerMore.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        ivMoreSeller.setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvContactSeller.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvViewStore.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        rattingOne.setProgressColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        rattingTwo.setProgressColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        rattingThree.setProgressColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        rattingFour.setProgressColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        rattingFive.setProgressColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
    }

    @OnClick(R.id.tvRateReview)
    public void RateReviewClick() {
        int img = 0;
        Intent i = new Intent(this, RateAndReviewActivity.class);
        i.putExtra(RequestParamUtils.PRODUCT_NAME, tvProductName.getText().toString());
        i.putExtra(RequestParamUtils.PRODUCT_ID, String.valueOf(categoryList.id + ""));
        if (categoryList.images.size() > 0 || categoryList.images.size() == 1) {
            i.putExtra(RequestParamUtils.IMAGEURL, categoryList.images.get(0).src);
        }
        startActivity(i);

    }

    private void setData() {
        if (categoryList != null && categoryList.rewardMessage != null && !categoryList.rewardMessage.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvReward.setText(Html.fromHtml(categoryList.rewardMessage, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvReward.setText(Html.fromHtml(categoryList.rewardMessage));
            }
            tvReward.setVisibility(View.VISIBLE);
        } else {
            tvReward.setVisibility(View.GONE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            tvProductName.setText(categoryList.name + "");
            tvProductName.setText(Html.fromHtml(categoryList.name + "", Html.FROM_HTML_MODE_LEGACY));
        } else {
//            tvProductName.setText(categoryList.name + "");
            tvProductName.setText(Html.fromHtml(categoryList.name + ""));
        }

        setPrice(categoryList.priceHtml);
        if (categoryList != null && categoryList.averageRating != null && categoryList.averageRating.equals("")) {
            tvRatting.setText("0");

        } else {
            if (categoryList.averageRating != null) {
                tvRatting.setText(Constant.setDecimalTwo(Double.parseDouble(categoryList.averageRating)));
            }


        }
        if (categoryList.inStock) {
            tvAvailibility.setText(R.string.in_stock);
            tvAvailibility.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        } else {
            tvAvailibility.setText(R.string.out_of_stock);
            tvAvailibility.setTextColor(Color.RED);
            tvBuyNow.setAlpha((float) 0.6);
            tvBuyNow.setClickable(false);
            tvCart.setAlpha((float) 0.6);
            tvCart.setClickable(false);
        }
        setSellerInformation();
        setProductDescription();
        setVpProductImages();
        if (categoryList.attributes.size() > 0) {
            llColor.setVisibility(View.VISIBLE);
            setColorData();
            String text = categoryList.attributes.get(0).name.substring(0, 1).toUpperCase() + categoryList.attributes.get(0).name.substring(1).toLowerCase();
            tvColor.setText(categoryList.attributes.get(0).options.size() + " " + text);
        } else {
            llColor.setVisibility(View.GONE);
        }

        if (!categoryList.shortDescription.equals("")) {
            llQuickOverView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDescription.setText(Html.fromHtml(categoryList.shortDescription, Html.FROM_HTML_MODE_COMPACT));

            } else {
                tvDescription.setText(Html.fromHtml(categoryList.shortDescription));
            }
        }
        showBackButton();
        showSearch();
        showCart();
        settvImage();
        setReviewData();
        if (categoryList.type.equals("variable")) {
            getVariation();
            llColor.setVisibility(View.VISIBLE);
        } else if (categoryList.type.equals("simple")) {
            if (databaseHelper.getProductFromCartById(categoryList.id + "") != null) {
                tvCart.setText(getResources().getString(R.string.go_to_cart));
            } else {
                tvCart.setText(getResources().getString(R.string.add_to_Cart));
            }
            CheckSimpleSelector.setSelectList();
            showSimpleDialog();
            for (int i = 0; i < categoryList.attributes.size(); i++) {
                CheckSimpleSelector.selectedList.put(categoryList.attributes.get(i).name, categoryList.attributes.get(i).options.get(0));
            }
            if (categoryList.attributes.size() > 0) {
                productColorAdapter.addAll(categoryList.attributes.get(0).options);
                productColorAdapter.getDialogList(categoryList.attributes);
                productColorAdapter.setSimpleDialog(productSimpleAdapter);
            }
            getReview();
            llColor.setVisibility(View.GONE);

        } else if (categoryList.type.equals("grouped")) {
            if (databaseHelper.getProductFromCartById(categoryList.id + "") != null) {
                tvCart.setText(getResources().getString(R.string.go_to_cart));
            } else {
                tvCart.setText(getResources().getString(R.string.add_to_Cart));
            }
            String groupis = "";
            for (int i = 0; i < categoryList.groupedProducts.size(); i++) {
                if (groupis.equals("")) {
                    groupis = groupis + categoryList.groupedProducts.get(i);
                } else {
                    groupis = groupis + "," + categoryList.groupedProducts.get(i);
                }
            }
            getGroupProducts(groupis);
            setRvGroupProduct();
        } else if (categoryList.type.equals("external")) {
            finish();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(categoryList.externalUrl));
            startActivity(browserIntent);
        }
    }

    public void setPrice(String price) {
        if (price == null) {
            price = categoryList.priceHtml;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPrice.setText(Html.fromHtml(price, Html.FROM_HTML_MODE_COMPACT));

        } else {
            tvPrice.setText(Html.fromHtml(price + " ") + "");
        }
        tvPrice.setTextSize(15);

        if (!tvPrice.getText().toString().contains("???") && tvPrice.getText().toString().contains(" ")) {
            String[] array = tvPrice.getText().toString().split(" ");
            if (array.length == 2) {
                String firstPrice = array[0];
                String seconfPrice = array[1];

                String htmlText = "<html><font color='#8E8E8E'>" + " " + firstPrice + "</font></html>";
                String htmlText1 = "<html><font color='" + getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR) + "'>" + " " + seconfPrice + "</font></html>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvPrice.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
                    tvPrice1.setText(Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_COMPACT));

                } else {
                    tvPrice.setText(Html.fromHtml(htmlText));
                    tvPrice1.setText(Html.fromHtml(htmlText1));
                }
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvPrice1.setText(tvPrice.getText().toString());
                tvPrice1.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
                tvPrice.setText("");
            }

        } else {
            tvPrice1.setText(tvPrice.getText().toString());
            tvPrice1.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
            tvPrice.setText("");
        }
    }

    private void setProductDescription() {
        if (categoryList.description != null && !categoryList.description.equals("")) {
            llProductDescription.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvProductDescription.setText(Html.fromHtml(categoryList.description, Html.FROM_HTML_MODE_COMPACT));

            } else {
                tvProductDescription.setText(Html.fromHtml(categoryList.description));
            }
        }
    }


    private void setSellerInformation() {
        if (categoryList.sellerInfo != null && categoryList.sellerInfo.isSeller) {
            llIsSeller.setVisibility(View.VISIBLE);

            if (categoryList.sellerInfo.contactSeller) {
                tvContactSeller.setClickable(true);
            } else {
                tvContactSeller.setClickable(false);
            }
        } else {
            llIsSeller.setVisibility(View.GONE);
        }


        if (categoryList != null && categoryList.sellerInfo != null) {
            tvSellerName.setText(categoryList.sellerInfo.storeName == null ? "" : categoryList.sellerInfo.storeName);
            if (categoryList.sellerInfo.storeTnc == null) {
                tvSellerMore.setVisibility(View.GONE);
                tvSellerContent.setVisibility(View.GONE);
            } else {
                tvSellerMore.setVisibility(View.VISIBLE);
                tvSellerContent.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvSellerContent.setText(Html.fromHtml(categoryList.sellerInfo.storeTnc, Html.FROM_HTML_MODE_COMPACT) + "");
                } else {
                    tvSellerContent.setText(Html.fromHtml(categoryList.sellerInfo.storeTnc) + "");
                }
            }
        }
    }


    public void getVariation() {

        GetApi getApi = new GetApi(this, "getVariation", this);
        getApi.callGetApi(new URLS().WOO_MAIN_URL + new URLS().WOO_PRODUCT_URL + "/" + categoryList.id + "/" + new URLS().WOO_VARIATION);
    }

    public void getReview() {

        GetApi getApi = new GetApi(this, "getReview", this);
        getApi.setisDialogShow(false);
        getApi.callGetApi(new URLS().WOO_MAIN_URL + new URLS().WOO_PRODUCT_URL + "/" + categoryList.id + "/" + new URLS().WOO_REVIEWS);
    }


    public void getGroupProducts(String groupid) {

        PostApi postApi = new PostApi(ProductDetailActivity.this, "getGroupProducts", this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RequestParamUtils.INCLUDE, groupid);
            jsonObject.put(RequestParamUtils.PAGE, page);
            postApi.callPostApi(new URLS().PRODUCT_URL, jsonObject.toString());
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }
    }

    @Override
    public void onResponse(String response, String methodName) {


        if (methodName.equals("getVariation")) {
            if (response != null && response.length() > 0) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    variationList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String jsonResponse = jsonArray.get(i).toString();
                        Variation variationRider = new Gson().fromJson(
                                jsonResponse, new TypeToken<Variation>() {
                                }.getType());
                        variationList.add(variationRider);
                    }
                    showDialog();
                    productColorAdapter.addAllVariationList(variationList, productVariationAdapter);
                    productColorAdapter.addAll(categoryList.attributes.get(0).options);

                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
                getReview();
            }
        } else if (methodName.equals("getGroupProducts")) {
            if (response != null && response.length() > 0) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<CategoryList> categoryLists = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String jsonResponse = jsonArray.get(i).toString();
                        CategoryList categoryListRider = new Gson().fromJson(
                                jsonResponse, new TypeToken<CategoryList>() {
                                }.getType());
                        categoryLists.add(categoryListRider);
                    }
                    groupProductAdapter.addAll(categoryLists);
                    if (categoryLists.size() > 0) {
                        rvGroupProduct.setVisibility(View.VISIBLE);
                    } else {
                        rvGroupProduct.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
            getReview();
        } else if (methodName.equals("getReview")) {
            if (response != null && response.length() > 0) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<ProductReview> reviewList = new ArrayList<>();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonResponse = jsonArray.get(i).toString();
                            ProductReview productReviewRider = new Gson().fromJson(
                                    jsonResponse, new TypeToken<ProductReview>() {
                                    }.getType());
                            reviewList.add(productReviewRider);

                            if (productReviewRider.rating == 5) {
                                fiveRate = fiveRate + 1;
                            } else if (productReviewRider.rating == 4) {
                                fourRate = fourRate + 1;
                            } else if (productReviewRider.rating == 3) {
                                threeRate = threeRate + 1;
                            } else if (productReviewRider.rating == 2) {
                                twoRate = twoRate + 1;
                            } else if (productReviewRider.rating == 1) {
                                oneRate = oneRate + 1;
                            }
                        }
                        setRate(reviewList.size());
//                        llReview.setVisibility(View.VISIBLE);
//                    } else {
//                        llReview.setVisibility(View.GONE);
                    }
                    reviewAdapter.addAll(reviewList);
                    dismissProgress();

                } catch (Exception e) {
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        } else if (methodName.equals("removeWishList") || methodName.equals("addWishList")) {
            dismissProgress();
        } else if (methodName.equals("addToAbandondCart")) {
            Log.e("Response is ", response);
        }
    }


    private void setRate(int totalReview) {
        tvAverageRatting.setText(Constant.setDecimalTwo(Double.valueOf(categoryList.averageRating)) + "/5");
        ratingBar.setRating(Float.parseFloat(categoryList.averageRating));
        rattingFive.setProgress(fiveRate / totalReview);
        rattingFour.setProgress(fourRate / totalReview);
        rattingThree.setProgress(threeRate / totalReview);
        rattingTwo.setProgress(twoRate / totalReview);
        rattingOne.setProgress(oneRate / totalReview);
        rattingOne.setMax(totalReview);
        rattingTwo.setMax(totalReview);
        rattingThree.setMax(totalReview);
        rattingFour.setMax(totalReview);
        rattingFive.setMax(totalReview);

    }

    private void setVpProductImages() {
        addBottomDots(0, categoryList.images.size());
        productImageViewPagerAdapter = new ProductImageViewPagerAdapter(this, categoryList.images.size(), categoryList.id);
        vpProductImages.setAdapter(productImageViewPagerAdapter);
        productImageViewPagerAdapter.addAll(categoryList.images);
        vpProductImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, categoryList.images.size());
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage, int length) {
        layoutDots.removeAllViews();
        dots = new TextView[length];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.gray));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
    }

    public void setRvGroupProduct() {
        groupProductAdapter = new GroupProductAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvGroupProduct.setLayoutManager(mLayoutManager);
        rvGroupProduct.setAdapter(groupProductAdapter);
        rvGroupProduct.setNestedScrollingEnabled(false);
    }


    public void setColorData() {
        productColorAdapter = new ProductColorAdapter(ProductDetailActivity.this, ProductDetailActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvColor.setLayoutManager(mLayoutManager);
        rvColor.setAdapter(productColorAdapter);
        rvColor.setNestedScrollingEnabled(false);
        productColorAdapter.addAll(categoryList.attributes.get(0).options);
        productColorAdapter.setType(categoryList.type);
        productColorAdapter.getDialogList(categoryList.attributes);
    }

    public void setReviewData() {
        reviewAdapter = new ReviewAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReview.setLayoutManager(mLayoutManager);
        rvReview.setAdapter(reviewAdapter);
        rvReview.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {
        if (databaseHelper.getVariationProductFromCart(getCartVariationProduct())) {
            tvCart.setText(getResources().getString(R.string.go_to_cart));
        } else {
            tvCart.setText(getResources().getString(R.string.add_to_Cart));
        }
        changePrice();
    }

    @OnClick(R.id.tvMoreQuickOverview)
    public void tvMoreQuickOverviewClick() {
        Intent intent = new Intent(this, ProductQuickDetailActivity.class);
        intent.putExtra("title", getString(R.string.quick_overviews));
        intent.putExtra("name", categoryList.name + "");
        intent.putExtra("description", categoryList.shortDescription + "");
        if (categoryList.images.size() > 0) {
            intent.putExtra("image", categoryList.images.get(0).src);
        } else {
            intent.putExtra("image", "");
        }
        startActivity(intent);
    }

    @OnClick(R.id.tvMoreDetail)
    public void tvMoreDetailClick() {
        Intent intent = new Intent(this, ProductQuickDetailActivity.class);
        intent.putExtra("title", getString(R.string.detail));
        intent.putExtra("name", categoryList.name + "");
        intent.putExtra("description", categoryList.description + "");
        if (categoryList.images.size() > 0) {
            intent.putExtra("image", categoryList.images.get(0).src);
        } else {
            intent.putExtra("image", "");
        }
        startActivity(intent);
    }

    @OnClick(R.id.tvSellerMore)
    public void tvSellerMoreClick() {
        Intent intent = new Intent(this, SellerMoreInfoActivity.class);
        intent.putExtra("data", categoryList.sellerInfo.storeTnc);
        startActivity(intent);
    }

    @OnClick(R.id.tvCart)
    public void tvCartClick() {
        if (categoryList.type.equals("variable")) {

            if (!isDialogOpen) {
                alertDialog.show();
                isDialogOpen = true;
            } else {
                if (!new CheckIsVariationAvailable().isVariationAvailbale(ProductDetailActivity.combination, variationList, categoryList.attributes)) {
                    toast.showToast(getString(R.string.variation_not_available));
                    toast.showRedbg();
                } else {
                    if (getCartVariationProduct() != null) {
                        Cart cart = getCartVariationProduct();
                        if (databaseHelper.getVariationProductFromCart(cart)) {
                            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                            intent.putExtra("buynow", 0);
                            startActivity(intent);
                        } else {
                            databaseHelper.addVariationProductToCart(getCartVariationProduct());
                            showCart();
                            toast.showToast(getString(R.string.item_added_to_your_cart));
                            toast.showBlackbg();
                            tvCart.setText(getResources().getString(R.string.go_to_cart));
                        }
                    } else {
                        toast.showToast(getString(R.string.variation_not_available));
                        toast.showRedbg();
                    }
                }
            }
        } else if (categoryList.type.equals("simple")) {

            JSONObject object = new JSONObject();
            try {
                for (int i = 0; i < categoryList.attributes.size(); i++) {
                    object.put(categoryList.attributes.get(i).name, CheckSimpleSelector.selectedList.get(categoryList.attributes.get(i).name));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Cart cart = new Cart();
            cart.setQuantity(1);
            cart.setVariation(object.toString());
            cart.setProduct(new Gson().toJson(categoryList));
            cart.setVariationid(0);
            cart.setProductid(categoryList.id + "");
            cart.setBuyNow(0);
            if (databaseHelper.getProductFromCartById(categoryList.id + "") != null) {
                databaseHelper.addToCart(cart);
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                intent.putExtra("buynow", 0);
                startActivity(intent);
            } else {
                databaseHelper.addToCart(cart);
                showCart();
                toast.showToast(getString(R.string.item_added_to_your_cart));
                toast.showBlackbg();
                tvCart.setText(getResources().getString(R.string.go_to_cart));
            }


        } else if (categoryList.type.equals("grouped")) {

            for (int i = 0; i < groupProductAdapter.getList().size(); i++) {
                if (groupProductAdapter.getList().get(i).type.equals("simple")) {
                    JSONObject object = new JSONObject();
                    try {
                        for (int j = 0; j < groupProductAdapter.getList().get(i).attributes.size(); j++) {
                            object.put(groupProductAdapter.getList().get(i).attributes.get(j).name, groupProductAdapter.getList().get(i).attributes.get(j).options.get(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Cart cart = new Cart();
                    cart.setQuantity(1);
                    cart.setVariation(object.toString());
                    cart.setProduct(new Gson().toJson(groupProductAdapter.getList().get(i)));
                    cart.setVariationid(0);
                    cart.setProductid(groupProductAdapter.getList().get(i).id + "");
                    cart.setBuyNow(0);
                    if (databaseHelper.getProductFromCartById(categoryList.id + "") != null) {
                        databaseHelper.addToCart(cart);
                        Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                        intent.putExtra("buynow", 0);
                        startActivity(intent);
                    } else {
                        databaseHelper.addToCart(cart);
                        showCart();
                        toast.showToast(getString(R.string.item_added_to_your_cart));
                        toast.showBlackbg();
                        tvCart.setText(getResources().getString(R.string.go_to_cart));
                    }


                }
            }
        }
    }

    public Cart getCartVariationProduct() {

        List<String> list = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            for (int i = 0; i < combination.size(); i++) {
                String value = combination.get(i);
                String[] valuearray = new String[0];
                if (value.contains("->")) {
                    valuearray = value.split("->");
                }
                if (valuearray.length > 0) {
                    object.put(valuearray[0], valuearray[1]);
                }
                list.add(combination.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Cart cart = new Cart();
        cart.setQuantity(1);
        cart.setVariation(object.toString());
        categoryList.priceHtml = CheckIsVariationAvailable.pricehtml;
        categoryList.price = CheckIsVariationAvailable.price + "";
        cart.setProduct(new Gson().toJson(categoryList));
        cart.setVariationid(new CheckIsVariationAvailable().getVariationid(variationList, list));
        cart.setProductid(categoryList.id + "");
        cart.setBuyNow(0);
        return cart;

    }


    @OnClick(R.id.tvContactSeller)
    public void tvContactSellerClick() {
        Intent intent = new Intent(this, ContactSellerActivity.class);
        intent.putExtra(RequestParamUtils.ID, categoryList.sellerInfo.sellerId);
        startActivity(intent);
    }

    @OnClick(R.id.tvViewStore)
    public void tvViewStoreClick() {
        sellerRedirection();
    }

    @OnClick(R.id.tvSellerName)
    public void tvSellerNameClick() {
        sellerRedirection();
    }

    public void sellerRedirection() {
        Intent intent = new Intent(this, SellerInfoActivity.class);
        intent.putExtra(RequestParamUtils.ID, categoryList.sellerInfo.sellerId);
        startActivity(intent);
    }

    @OnClick(R.id.ivShare)
    public void ivShareClick() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, categoryList.permalink);
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    @OnClick(R.id.tvBuyNow)
    public void tvBuyNowClick() {

        if (categoryList.type.equals("variable")) {
            if (!isDialogOpen) {
                if (alertDialog != null) {
                    alertDialog.show();
                }
                isDialogOpen = true;
            } else {
                if (!new CheckIsVariationAvailable().isVariationAvailbale(ProductDetailActivity.combination, variationList, categoryList.attributes)) {
                    toast.showToast(getString(R.string.variation_not_available));
                    toast.showRedbg();
                } else {
                    toast.cancelToast();
                    List<String> list = new ArrayList<>();
                    JSONObject object = new JSONObject();
                    try {
                        for (int i = 0; i < combination.size(); i++) {
                            String value = combination.get(i);
                            String[] valuearray = new String[0];
                            if (value.contains("->")) {
                                valuearray = value.split("->");
                            }
                            if (valuearray.length > 0) {
                                object.put(valuearray[0], valuearray[1]);
                            }
                            list.add(combination.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Cart cart = new Cart();
                    cart.setQuantity(1);
                    cart.setVariation(object.toString());
                    categoryList.priceHtml = CheckIsVariationAvailable.pricehtml;
                    categoryList.price = CheckIsVariationAvailable.price + "";
                    cart.setProduct(new Gson().toJson(categoryList));
                    cart.setVariationid(new CheckIsVariationAvailable().getVariationid(variationList, list));
                    cart.setProductid(categoryList.id + "");
                    addToCart(cart, categoryList.id + "");
                }
            }
        } else if (categoryList.type.equals("simple")) {
            JSONObject object = new JSONObject();
            try {
                for (int i = 0; i < categoryList.attributes.size(); i++) {
                    object.put(categoryList.attributes.get(i).name, CheckSimpleSelector.selectedList.get(categoryList.attributes.get(i).name));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Cart cart = new Cart();
            cart.setQuantity(1);
            cart.setVariation(object.toString());
            cart.setProduct(new Gson().toJson(categoryList));
            cart.setVariationid(0);
            cart.setProductid(categoryList.id + "");
            addToCart(cart, categoryList.id + "");
        } else if (categoryList.type.equals("grouped")) {

            for (int i = 0; i < groupProductAdapter.getList().size(); i++) {
                if (groupProductAdapter.getList().get(i).type.equals("simple")) {
                    JSONObject object = new JSONObject();
                    try {
                        for (int j = 0; j < groupProductAdapter.getList().get(i).attributes.size(); j++) {
                            object.put(groupProductAdapter.getList().get(i).attributes.get(j).name, groupProductAdapter.getList().get(i).attributes.get(j).options.get(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Cart cart = new Cart();
                    cart.setQuantity(1);
                    cart.setVariation(object.toString());
                    cart.setProduct(new Gson().toJson(groupProductAdapter.getList().get(i)));
                    cart.setVariationid(0);
                    cart.setProductid(groupProductAdapter.getList().get(i).id + "");
                    addToCart(cart, categoryList.id + "");
                }
            }
        }


    }

    @OnClick(R.id.llDialog)
    public void lLDialogClick() {

        if (categoryList.type.equals("variable")) {
            if (alertDialog != null) {
                alertDialog.show();
            }

//            productColorAdapter.addAllVariationList(variationList);
        } else if (categoryList.type.equals("simple")) {
            alertDialog.show();
        }
    }


    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_variation, null);
        dialogBuilder.setView(dialogView);

        rvProductVariation = (RecyclerView) dialogView.findViewById(R.id.rvProductVariation);
        TextViewRegular tvDone = (TextViewRegular) dialogView.findViewById(R.id.tvDone);
        TextViewRegular tvCancel = (TextViewRegular) dialogView.findViewById(R.id.tvCancel);

        productVariationAdapter = new ProductVariationAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProductVariation.setLayoutManager(mLayoutManager);
        rvProductVariation.setAdapter(productVariationAdapter);
        rvProductVariation.setNestedScrollingEnabled(false);
        productVariationAdapter.addAll(categoryList.attributes);
        productVariationAdapter.addAllVariationList(variationList);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//        alertDialog.show();
        tvCancel.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvDone.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                if (!new CheckIsVariationAvailable().isVariationAvailbale(ProductDetailActivity.combination, variationList, categoryList.attributes)) {
                    toast.showToast(getString(R.string.combition_doesnt_exist));
                } else {
                    toast.cancelToast();
                    alertDialog.dismiss();
                    if (databaseHelper.getVariationProductFromCart(getCartVariationProduct())) {
                        tvCart.setText(getResources().getString(R.string.go_to_cart));
                    } else {
                        tvCart.setText(getResources().getString(R.string.add_to_Cart));
                    }
                    changePrice();
                }
            }
        });
        changePrice();
    }

    public void showSimpleDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_variation, null);
        dialogBuilder.setView(dialogView);
        RecyclerView rvProductVariation = (RecyclerView) dialogView.findViewById(R.id.rvProductVariation);
        TextViewRegular tvDone = (TextViewRegular) dialogView.findViewById(R.id.tvDone);
        TextViewRegular tvCancel = (TextViewRegular) dialogView.findViewById(R.id.tvCancel);
        productSimpleAdapter = new ProductSimpleAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProductVariation.setLayoutManager(mLayoutManager);
        rvProductVariation.setAdapter(productSimpleAdapter);
        rvProductVariation.setNestedScrollingEnabled(false);
        productSimpleAdapter.addAll(categoryList.attributes);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        tvCancel.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvDone.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ProductColorAdapter.selectedpos = pos;
                productColorAdapter.notifyDataSetChanged();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                pos = ProductColorAdapter.selectedpos;
                productColorAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addToCart(Cart cart, String id) {
        showCart();
        cart.setBuyNow(1);
        databaseHelper.addToCart(cart);

        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra(RequestParamUtils.ID, categoryList.id + "");
        intent.putExtra("buynow", 1);
        startActivity(intent);
    }


    public void changePrice() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < combination.size(); i++) {
            list.add(combination.get(i));
        }

        new CheckIsVariationAvailable().getVariationid(variationList, list);
        if (CheckIsVariationAvailable.pricehtml != null) {
            setPrice(CheckIsVariationAvailable.pricehtml);
        }
    }


    @OnClick(R.id.ivWishList)
    public void ivWishListClick() {
        if (databaseHelper.getWishlistProduct(categoryList.id + "")) {
            ivWishList.setChecked(false);
            String userid = getPreferences().getString(RequestParamUtils.ID, "");
            if (!userid.equals("")) {
                removeWishList(true, userid, categoryList.id + "");
            }
            databaseHelper.deleteFromWishList(categoryList.id + "");
        } else {
            ivWishList.setChecked(true);
            ivWishList.playAnimation();
            WishList wishList = new WishList();
            wishList.setProduct(new Gson().toJson(categoryList));
            wishList.setProductid(categoryList.id + "");
            databaseHelper.addToWishList(wishList);
            String userid = getPreferences().getString(RequestParamUtils.ID, "");
            if (!userid.equals("")) {
                removeWishList(true, userid, categoryList.id + "");
            }
        }
    }

    public void removeWishList(boolean isDialogShow, String userid, String productid) {
        PostApi postApi = new PostApi(ProductDetailActivity.this, "removeWishList", this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RequestParamUtils.USER_ID, userid);
            jsonObject.put(RequestParamUtils.PRODUCT_ID, productid);
            postApi.setisDialogShow(isDialogShow);
            postApi.callPostApi(new URLS().REMOVE_FROM_WISHLIST, jsonObject.toString());
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showCart();
        if (databaseHelper.getProductFromCartById(categoryList.id + "") != null) {
            tvCart.setText(getResources().getString(R.string.go_to_cart));
        } else {
            tvCart.setText(getResources().getString(R.string.add_to_Cart));
        }
        Constant.CATEGORYDETAIL = categoryList;
    }


}




