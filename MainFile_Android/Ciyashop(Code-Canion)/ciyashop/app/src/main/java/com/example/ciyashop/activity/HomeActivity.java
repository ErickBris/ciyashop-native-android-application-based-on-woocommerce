package com.example.ciyashop.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.adapter.BannerViewPagerAdapter;
import com.example.ciyashop.adapter.CategoryAdapter;
import com.example.ciyashop.adapter.HomeTopCategoryAdapter;
import com.example.ciyashop.adapter.MostPopularAdapter;
import com.example.ciyashop.adapter.NavigationDrawerAdapter;
import com.example.ciyashop.adapter.RecentViewAdapter;
import com.example.ciyashop.adapter.SixReasonAdapter;
import com.example.ciyashop.adapter.SpecialOfferAdapter;
import com.example.ciyashop.adapter.VerticalBannerAdapter;
import com.example.ciyashop.customview.CustomLinearLayoutManager;
import com.example.ciyashop.customview.SpacesItemDecoration;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.CategoryList;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.model.NavigationList;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements OnItemClickListner, OnResponseListner {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.drawerListView)
    ListView drawerListView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @BindView(R.id.rvTopCategory)
    RecyclerView rvTopCategory;

    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;

    @BindView(R.id.rvVerticalBanner)
    RecyclerView rvVerticalBanner;

    @BindView(R.id.rvMostPopular)
    RecyclerView rvMostPopular;

    @BindView(R.id.rvSpecialOffer)
    RecyclerView rvSpecialOffer;

    @BindView(R.id.rvSixReason)
    RecyclerView rvSixReason;

    @BindView(R.id.rvRecentOffer)
    RecyclerView rvRecentOffer;

    @BindView(R.id.vpBanner)
    ViewPager vpBanner;

    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;

    @BindView(R.id.tvTimer)
    TextViewRegular tvTimer;

    @BindView(R.id.ivBack)
    ImageView ivDrawer;

    @BindView(R.id.llMain)
    LinearLayout llMain;

    @BindView(R.id.llTopCategory)
    LinearLayout llTopCategory;

    @BindView(R.id.llBanner)
    LinearLayout llBanner;

    @BindView(R.id.llCategory)
    LinearLayout llCategory;

    @BindView(R.id.llVerticalBanner)
    LinearLayout llVerticalBanner;

    @BindView(R.id.llMostPopular)
    LinearLayout llMostPopular;

    @BindView(R.id.llSpecialOffer)
    LinearLayout llSpecialOffer;

    @BindView(R.id.llSixReason)
    LinearLayout llSixReason;

    @BindView(R.id.llRecentView)
    LinearLayout llRecentView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.ablHome)
    AppBarLayout ablHome;

    @BindView(R.id.ivNotification)
    ImageView ivNotification;

    @BindView(R.id.tvViewAllMostPopular)
    TextViewRegular tvViewAllMostPopular;

    @BindView(R.id.tvViewAllSpecialDeal)
    TextViewRegular tvViewAllSpecialDeal;

    @BindView(R.id.tvSixResonTitle)
    TextViewBold tvSixResonTitle;

    @BindView(R.id.ivTimer)
    ImageView ivTimer;

    private TextView[] dots;
    private int[] layouts;
    private int currentPosition;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private HomeTopCategoryAdapter homeTopCategoryAdapter;
    private CategoryAdapter categoryAdapter;
    private VerticalBannerAdapter verticalBannerAdapter;
    private MostPopularAdapter mostPopularAdapter;
    private SpecialOfferAdapter specialOfferAdapter;
    private SixReasonAdapter sixReasonAdapter;
    private RecentViewAdapter recentViewAdapter;
    private View listHeaderView;
    private TextViewRegular tvName;
    private boolean ishead = false;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private DatabaseHelper databaseHelper;
    private boolean isAutoScroll = false, isSpecialDeal = false;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    Home homeRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setHomecolorTheme(getPreferences().getString(Constant.HEADER_COLOR, Constant.HEAD_COLOR));
        setScreenLayoutDirection();
        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer));
        // Get token and Save Notification Token
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor pre = getPreferences().edit();
        pre.putString(RequestParamUtils.NOTIFICATION_TOKEN, token);
        pre.commit();
        getHomeData();
        initDrawer();
        swipeView();
        settvImage();
        showNotification();
        showCart();
        setHomeCategoryData();
        setView();
        categoryData();
        verticalBannerData();
        setMostPopularAdapter();
        setSpecialOfferAdapter();
        setSixReasonAdapter();
        setRecentViewAdapter();
        getRecentData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.IS_CURRENCY_SET) {
            getHomeData();
            databaseHelper.clearRecentItem();
            databaseHelper.clearCart();
            Constant.IS_CURRENCY_SET = false;
        }
    }

    // TODO: Remove this code after the UrlTable2 has been checked in.
    public void getRecentData() {
        databaseHelper = new DatabaseHelper(HomeActivity.this);
        List<CategoryList> recentList = databaseHelper.getRecentViewList();
        recentViewAdapter.addAll(recentList);
        if (recentList.size() > 0) {
            llRecentView.setVisibility(View.VISIBLE);
        } else {
            llRecentView.setVisibility(View.GONE);
        }
    }

    public void getHomeData() {

        PostApi postApi = new PostApi(this, "getHomeData", this);
        postApi.callPostApi(new URLS().HOME + getPreferences().getString(RequestParamUtils.CurrencyText, ""), "");
    }

    public void swipeView() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.orange,
                R.color.red,
                R.color.blue
        );
    }

    public void initDrawer() {

        if (Constant.IS_RTL) {
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.FILL_PARENT);
            params.gravity = Gravity.RIGHT;
            drawerListView.setLayoutParams(params);
        } else {
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.FILL_PARENT);
            params.gravity = Gravity.LEFT;
            drawerListView.setLayoutParams(params);
        }


        LayoutInflater inflater = getLayoutInflater();
        listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
        tvName = (TextViewRegular) listHeaderView.findViewById(R.id.tvName);

        if (!ishead) {
            drawerListView.addHeaderView(listHeaderView);
            ishead = true;
        }
        navigationDrawerAdapter = new NavigationDrawerAdapter(this);
        drawerListView.setAdapter(navigationDrawerAdapter);
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.IS_RTL) {
                    drawer_layout.openDrawer(Gravity.RIGHT);
                } else {
                    drawer_layout.openDrawer(Gravity.LEFT);
                }

            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawer_layout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                selectItemFragment(position - 1);
            }
        });
    }

    private void selectItemFragment(int position) {

        if (position == -1) {

        } else {
            if (position < navigationDrawerAdapter.getSeprater()) {
                Intent intent = new Intent(this, CategoryListActivity.class);
                intent.putExtra(RequestParamUtils.CATEGORY, navigationDrawerAdapter.getList().get(position).mainCatId);
                intent.putExtra(RequestParamUtils.IS_WISHLIST_ACTIVE, Constant.IS_WISH_LIST_ACTIVE);
                startActivity(intent);
            } else if (position == navigationDrawerAdapter.getSeprater()) {
                Intent intent = new Intent(this, SearchCategoryListActivity.class);
                startActivity(intent);
            } else {
                selectlocalFragment(navigationDrawerAdapter.getList().get(position).mainCatName);

            }
        }
        drawerListView.setItemChecked(position, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer_layout.closeDrawer(drawerListView);

            }
        }, 200);
    }


    public void selectlocalFragment(String name) {

        if (name.equals(getResources().getString(R.string.notification))) {
            Intent notificationIntent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(notificationIntent);
        } else if (name.equals(getResources().getString(R.string.my_reward))) {
            Intent rewardIntent = new Intent(HomeActivity.this, RewardsActivity.class);
            startActivity(rewardIntent);
        } else if (name.equals(getResources().getString(R.string.my_cart))) {
            Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(cartIntent);
        } else if (name.equals(getResources().getString(R.string.my_wish_list))) {
            Intent wishListIntent = new Intent(HomeActivity.this, WishListActivity.class);
            startActivity(wishListIntent);
        } else if (name.equals(getResources().getString(R.string.my_account))) {
            Intent accountIntent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(accountIntent);
        } else if (name.equals(getResources().getString(R.string.my_orders))) {
            if (getPreferences().getString(RequestParamUtils.ID, "").equals("")) {
                Intent myOrderIntent = new Intent(HomeActivity.this, LogInActivity.class);
                startActivity(myOrderIntent);
            } else {
                Intent myOrderIntent = new Intent(HomeActivity.this, MyOrderActivity.class);
                startActivity(myOrderIntent);
            }
        }

    }

    public void setHomeCategoryData() {
        homeTopCategoryAdapter = new HomeTopCategoryAdapter(this, this);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTopCategory.setLayoutManager(mLayoutManager);
        rvTopCategory.setAdapter(homeTopCategoryAdapter);
        rvTopCategory.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvTopCategory, false);
        rvTopCategory.setHasFixedSize(true);
        rvTopCategory.setItemViewCacheSize(20);
        rvTopCategory.setDrawingCacheEnabled(true);
        rvTopCategory.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public void categoryData() {
        categoryAdapter = new CategoryAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(mLayoutManager);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvCategory, false);
        rvCategory.setHasFixedSize(true);
        rvCategory.setItemViewCacheSize(20);
        rvCategory.setDrawingCacheEnabled(true);
        rvCategory.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public void verticalBannerData() {
        verticalBannerAdapter = new VerticalBannerAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvVerticalBanner.setLayoutManager(mLayoutManager);
        rvVerticalBanner.setAdapter(verticalBannerAdapter);
        rvVerticalBanner.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvVerticalBanner, false);
        rvVerticalBanner.setHasFixedSize(true);
        rvVerticalBanner.setItemViewCacheSize(20);
        rvVerticalBanner.setDrawingCacheEnabled(true);
        rvVerticalBanner.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvVerticalBanner.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }

    public void setMostPopularAdapter() {
        mostPopularAdapter = new MostPopularAdapter(this, this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvMostPopular.setLayoutManager(mLayoutManager);
        rvMostPopular.setAdapter(mostPopularAdapter);
        rvMostPopular.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvMostPopular, false);
        rvMostPopular.setHasFixedSize(true);
        rvMostPopular.setItemViewCacheSize(20);
        rvMostPopular.setDrawingCacheEnabled(true);
        rvMostPopular.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvMostPopular.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }

    public void setSpecialOfferAdapter() {
        specialOfferAdapter = new SpecialOfferAdapter(this, this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvSpecialOffer.setLayoutManager(mLayoutManager);
        rvSpecialOffer.setAdapter(specialOfferAdapter);
        rvSpecialOffer.setNestedScrollingEnabled(false);
        rvSpecialOffer.setClipToPadding(false);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.value_1);
        rvSpecialOffer.addItemDecoration(new SpacesItemDecoration(2, spacingInPixels, true, 0));
        ViewCompat.setNestedScrollingEnabled(rvSpecialOffer, false);
        rvSpecialOffer.setHasFixedSize(true);
        rvSpecialOffer.setItemViewCacheSize(20);
        rvSpecialOffer.setDrawingCacheEnabled(true);
        rvSpecialOffer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvSpecialOffer.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }

    public void setSixReasonAdapter() {
        sixReasonAdapter = new SixReasonAdapter(this, this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvSixReason.setLayoutManager(mLayoutManager);
        rvSixReason.setAdapter(sixReasonAdapter);
        rvSixReason.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvSixReason, false);
        rvSixReason.setHasFixedSize(true);
        rvSixReason.setItemViewCacheSize(20);
        rvSixReason.setDrawingCacheEnabled(true);
        rvSixReason.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public void setRecentViewAdapter() {
        recentViewAdapter = new RecentViewAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecentOffer.setLayoutManager(mLayoutManager);
        rvRecentOffer.setAdapter(recentViewAdapter);
        rvRecentOffer.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvRecentOffer, false);
        rvRecentOffer.setHasFixedSize(true);
        rvRecentOffer.setItemViewCacheSize(20);
        rvRecentOffer.setDrawingCacheEnabled(true);
        rvRecentOffer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvRecentOffer.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }


    private void setView() {
        bannerViewPagerAdapter = new BannerViewPagerAdapter(this);
        vpBanner.setAdapter(bannerViewPagerAdapter);
        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, vpBanner.getAdapter().getCount());
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void autoScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPosition == bannerViewPagerAdapter.getCount() - 1) {
                            currentPosition = 0;
                        } else {
                            currentPosition = currentPosition + 1;
                        }
                        vpBanner.setCurrentItem(currentPosition);
                        addBottomDots(currentPosition, bannerViewPagerAdapter.getCount());
                        autoScroll();
                    }
                }, 6000);

            }
        }, 1000);
    }

    private void addBottomDots(int currentPage, int lenght) {
        layoutDots.removeAllViews();
        dots = new TextView[lenght];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.gray));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0 && dots.length >= currentPage) {

            dots[currentPage].setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        }

    }


    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }

    @OnClick(R.id.tvViewAllMostPopular)
    public void tvViewAllMostPopularClick() {
        Intent intent = new Intent(HomeActivity.this, CategoryListActivity.class);
        intent.putExtra(RequestParamUtils.ORDER_BY, "popularity");
        intent.putExtra(RequestParamUtils.POSITION, 2);
        intent.putExtra(RequestParamUtils.IS_WISHLIST_ACTIVE, Constant.IS_WISH_LIST_ACTIVE);
        startActivity(intent);
    }

    @OnClick(R.id.tvViewAllSpecialDeal)
    public void tvViewAllSpecialDealClick() {

        Intent intent = new Intent(HomeActivity.this, CategoryListActivity.class);
        intent.putExtra(RequestParamUtils.DEAL_OF_DAY, getScheduledList());
        startActivity(intent);
    }


    @OnClick(R.id.etSearch)
    public void etSearchClick() {
        Intent intent = new Intent(HomeActivity.this, SearchFromHomeActivity.class);
        startActivity(intent);
    }

    public String getScheduledList() {
        String productid = "";
        for (int i = 0; i < specialOfferAdapter.getList().size(); i++) {
            if (productid.equals("")) {
                productid = specialOfferAdapter.getList().get(i).id;
            } else {
                productid = productid + "," + specialOfferAdapter.getList().get(i).id;
            }
        }
        return productid;
    }

    @Override
    public void onResponse(final String response, String methodName) {


        if (methodName.equals("getHomeData")) {
            if (response != null && response.length() > 0) {
                swipeContainer.setRefreshing(false);
                try {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    homeRider = gson.fromJson(
                            response, new TypeToken<Home>() {
                            }.getType());

                    setColorPreferences(homeRider.appColor.primaryColor, homeRider.appColor.secondaryColor, homeRider.appColor.headerColor);
                    setHomecolorTheme(getPreferences().getString(Constant.HEADER_COLOR, Constant.HEAD_COLOR));

                    if (homeRider.pgsAppContactInfo != null) {
                        if (homeRider.pgsAppContactInfo.addressLine1 != null) {
                            Constant.ADDRESS_LINE1 = homeRider.pgsAppContactInfo.addressLine1;
                        }
                        if (homeRider.pgsAppContactInfo.addressLine2 != null) {
                            Constant.ADDRESS_LINE2 = homeRider.pgsAppContactInfo.addressLine2;
                        }
                        if (homeRider.pgsAppContactInfo.email != null) {
                            Constant.EMAIL = homeRider.pgsAppContactInfo.email;
                        }
                        if (homeRider.pgsAppContactInfo.phone != null) {
                            Constant.PHONE = homeRider.pgsAppContactInfo.phone;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            Constant.IS_RTL = homeRider.isRtl;
                            getPreferences().edit().putBoolean(Constant.RTL, Constant.IS_RTL).commit();
                            String lang = homeRider.siteLanguage;
                            if (lang.contains("-")) {
                                String[] array = lang.split("-");
                                if (array.length > 0) {
                                    setLocale(array[0]);
                                } else {
                                    setLocale(homeRider.siteLanguage);
                                }
                            } else {
                                setLocale(homeRider.siteLanguage);
                            }
                            Constant.IS_CURRENCY_SWITCHER_ACTIVE = homeRider.isCurrencySwitcherActive;
                            Constant.IS_ORDER_TRACKING_ACTIVE = homeRider.isOrderTrackingActive;
                            Constant.IS_REWARD_POINT_ACTIVE = homeRider.isRewardPointActive;
                            if (Constant.IS_CURRENCY_SWITCHER_ACTIVE) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONObject currency_switcher = jsonObj.getJSONObject("currency_switcher");
                                    Constant.CurrencyList = new ArrayList<>();
                                    JSONArray namearray = currency_switcher.names();  //<<< get all keys in JSONArray
                                    for (int i = 0; i < namearray.length(); i++) {
                                        JSONObject c = currency_switcher.getJSONObject(namearray.get(i).toString());
                                        String name = c.getString("name");
                                        String symbol = c.getString("symbol");

                                        JSONObject obj = new JSONObject();
                                        obj.put(RequestParamUtils.NAME, name);
                                        obj.put(RequestParamUtils.SYMBOL, symbol);


                                        // adding contact to contact list
                                        Constant.CurrencyList.add(String.valueOf(obj));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Constant.IS_WISH_LIST_ACTIVE = homeRider.isWishlistActive;
                            Constant.SOCIALLINK = homeRider.pgsAppSocialLinks;
                            Constant.CURRENCYSYMBOLPOSTION = homeRider.priceFormateOptions.currencyPos;
                            Constant.CURRENCYSYMBOL = Html.fromHtml(homeRider.priceFormateOptions.currencySymbol).toString();
                            Constant.Decimal = homeRider.priceFormateOptions.decimals;
                            Constant.DECIMALSEPRETER = homeRider.priceFormateOptions.decimalSeparator;
                            Constant.THOUSANDSSEPRETER = homeRider.priceFormateOptions.thousandSeparator;

                            for (int i = 0; i < homeRider.allCategories.size(); i++) {
                                if (homeRider.allCategories.get(i).name.equals("Uncategorized")) {
                                    homeRider.allCategories.remove(i);
                                }
                            }
                            Constant.MAINCATEGORYLIST.clear();
                            Constant.MAINCATEGORYLIST.addAll(homeRider.allCategories);
                            setMainCategoryList(homeRider.mainCategory);
                            setSliderList(homeRider.mainSlider);
                            setCategoryList(homeRider.categoryBanners);
                            setVerticalBannerList(homeRider.bannerAd);
                            setMostPopularList(homeRider.popularProducts);
                            if (homeRider.scheduledSaleProducts.status.equals("success")) {
                                setSpecialOfferList(homeRider.scheduledSaleProducts.products);
                            } else {
                                llSpecialOffer.setVisibility(View.GONE);
                            }
                            if (homeRider.featureBoxStatus != null && homeRider.featureBoxStatus.equals("enable")) {
                                setSixReasonrList(homeRider.featureBox, homeRider.featureBoxHeading);
                            } else {
                                llSixReason.setVisibility(View.GONE);
                            }
                            llMain.setVisibility(View.VISIBLE);
                            Constant.APPLOGO = homeRider.appLogo;
                            Constant.APPLOGO_LIGHT = homeRider.appLogoLight;

                            SharedPreferences.Editor editor = getPreferences().edit();
                            editor.putString(Constant.APPLOGO, homeRider.appLogo);
                            editor.putString(Constant.APPLOGO_LIGHT, homeRider.appLogoLight);
                            editor.commit();

                            settvImage();

                            ablHome.setBackgroundColor(Color.parseColor("#60A727"));
                            tvViewAllMostPopular.setBackgroundColor(Color.parseColor("#60A727"));
                            tvViewAllSpecialDeal.setBackgroundColor(Color.parseColor("#60A727"));

                            Picasso.with(HomeActivity.this).load(homeRider.notificationIcon).into(ivNotification);
                            Picasso.with(HomeActivity.this).load(homeRider.clockIcon).into(ivTimer);
                            setThemeIconColor(getPreferences().getString(Constant.PRIMARY_COLOR, Constant.PRIMARY_COLOR));

                        }
                    });
                    dismissProgress();

                } catch (Exception e) {
                    dismissProgress();
                    if (response.equals("OAuthConnectionException")) {
                        getHomeData();
                    }
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setThemeIconColor(String color) {
        ivTimer.setColorFilter(Color.parseColor(color));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor(color));
        gradientDrawable.setCornerRadius(5);
        tvViewAllMostPopular.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        tvViewAllSpecialDeal.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
    }

    public void setColorPreferences(String primaryColor, String secondaryColor, String HeaderColor) {
        String colorSubString = (primaryColor.substring(primaryColor.lastIndexOf("#") + 1));
        SharedPreferences.Editor editor = getPreferences().edit();

        if (!primaryColor.equals("")) {
            editor.putString(Constant.APP_COLOR, primaryColor);
            editor.putString(Constant.APP_TRANSPARENT, "#aa" + colorSubString);
            editor.putString(Constant.APP_TRANSPARENT_VERY_LIGHT, "#44" + colorSubString);
        }
        if (!secondaryColor.equals("")) {
            editor.putString(Constant.SECOND_COLOR, secondaryColor);
        }
        if (!HeaderColor.equals("")) {
            editor.putString(Constant.HEADER_COLOR, HeaderColor);
        }
        editor.commit();
    }

    public void setMainCategoryList(List<Home.MainCategory> list) {
        if (list != null) {
            List<Home.MainCategory> mainCategoryList = new ArrayList<>();
            if (list.size() > 0) {

                if (list.size() > 5) {
                    for (int i = 0; i <= 5; i++) {
                        mainCategoryList.add(list.get(i));
                    }
                } else {

                    mainCategoryList.addAll(list);
                }

                Home.MainCategory mainCategory = new Home().getInstranceMainCategory();
                mainCategory.mainCatName = getString(R.string.more);
                mainCategoryList.add(mainCategory);
                homeTopCategoryAdapter.addAll(mainCategoryList);
                llTopCategory.setVisibility(View.VISIBLE);


            } else {
                llTopCategory.setVisibility(View.GONE);
            }
            navigationDrawerAdapter.setSepreter(mainCategoryList.size() - 1);
            List<Home.MainCategory> drawerList = new ArrayList<Home.MainCategory>();
            drawerList.addAll(mainCategoryList);
            for (int i = 0; i < NavigationList.getInstance(this).getImageList().size(); i++) {
                Home.MainCategory mainCategory = new Home().getInstranceMainCategory();
                mainCategory.mainCatName = NavigationList.getInstance(this).getTitleList().get(i);
                mainCategory.mainCatImage = NavigationList.getInstance(this).getImageList().get(i) + "";
                mainCategory.mainCatId = i + "";
                drawerList.add(mainCategory);
            }
            navigationDrawerAdapter.addAll(drawerList);
        } else {
            llTopCategory.setVisibility(View.GONE);
            navigationDrawerAdapter.setSepreter(0);
            List<Home.MainCategory> drawerList = new ArrayList<Home.MainCategory>();
            for (int i = 0; i < NavigationList.getInstance(this).getImageList().size(); i++) {
                Home.MainCategory mainCategory = new Home().getInstranceMainCategory();
                mainCategory.mainCatName = NavigationList.getInstance(this).getTitleList().get(i);
                mainCategory.mainCatImage = NavigationList.getInstance(this).getImageList().get(i) + "";
                mainCategory.mainCatId = i + "";
                drawerList.add(mainCategory);
            }
            navigationDrawerAdapter.addAll(drawerList);
        }

    }

    public void setSliderList(List<Home.MainSlider> list) {
        if (list != null) {
            if (list.size() > 0) {
                bannerViewPagerAdapter.addAll(list);
                if (!isAutoScroll) {
                    addBottomDots(0, vpBanner.getAdapter().getCount());
                    autoScroll();
                    isAutoScroll = true;
                }
                llBanner.setVisibility(View.VISIBLE);
            } else {
                llBanner.setVisibility(View.GONE);
            }
        } else {
            llBanner.setVisibility(View.GONE);
        }

    }

    public void setCategoryList(List<Home.CategoryBanner> list) {
        if (list != null) {
            if (list.size() > 0) {
                categoryAdapter.addAll(list);
                llCategory.setVisibility(View.VISIBLE);
            } else {
                llCategory.setVisibility(View.GONE);
            }
        } else {
            llCategory.setVisibility(View.GONE);
        }
    }

    public void setVerticalBannerList(List<Home.BannerAd> list) {
        if (list != null) {
            if (list.size() > 0) {
                verticalBannerAdapter.addAll(list);
                llVerticalBanner.setVisibility(View.VISIBLE);
            } else {
                llVerticalBanner.setVisibility(View.GONE);
            }
        } else {
            llVerticalBanner.setVisibility(View.GONE);
        }

    }

    public void setMostPopularList(List<Home.PopularProduct> list) {
        if (list != null) {
            if (list.size() > 0) {
                mostPopularAdapter.addAll(list);
                llMostPopular.setVisibility(View.VISIBLE);
            } else {
                llMostPopular.setVisibility(View.GONE);
            }
        } else {
            llMostPopular.setVisibility(View.GONE);
        }
    }

    public void setSpecialOfferList(List<Home.Product> list) {
        if (list != null) {
            String timer = "";
            if (list.size() > 0) {
                specialOfferAdapter.addAll(list);
                llSpecialOffer.setVisibility(View.VISIBLE);
            } else {
                llSpecialOffer.setVisibility(View.GONE);
            }

            if (list.size() > 0) {
                timer = list.get(0).dealLife.hours + ":" + list.get(0).dealLife.minutes + ":" + list.get(0).dealLife.seconds;
            }

            tvTimer.setText(timer);

            if (!isSpecialDeal) {
                isSpecialDeal = true;
                setTimer();
            }
        } else {
            llSpecialOffer.setVisibility(View.GONE);
        }

    }

    public void setSixReasonrList(List<Home.FeatureBox> list, String title) {
        if (list != null) {
            if (list.size() > 0) {
                sixReasonAdapter.addAll(list);

                if (list.size() == 1) {
                    if (list.get(0).featureContent.equals("")) {
                        llSixReason.setVisibility(View.GONE);
                    } else {

                        llSixReason.setVisibility(View.VISIBLE);

                    }
                } else {
                    llSixReason.setVisibility(View.VISIBLE);
                }

            } else {
                llSixReason.setVisibility(View.GONE);
            }

            tvSixResonTitle.setText(title);
        } else {
            llSixReason.setVisibility(View.GONE);
        }

    }

    private void setTimer() {
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                handler.postDelayed(this, delay);
                long time = convertInMilisecond(tvTimer.getText().toString()) - 1000;
                if (time == 0) {
                    llSpecialOffer.setVisibility(View.GONE);
                } else {
                    tvTimer.setText(convertInTimeFormet(time));
                }
            }
        }, delay);
    }

    private long convertInMilisecond(String time) {

        String[] tokens = time.split(":");
        int secondsToMs = Integer.parseInt(tokens[2]) * 1000;
        int minutesToMs = Integer.parseInt(tokens[1]) * 60000;
        int hoursToMs = Integer.parseInt(tokens[0]) * 3600000;
        long total = secondsToMs + minutesToMs + hoursToMs;
        return total;
    }

    private String convertInTimeFormet(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getRecentData();
        showCart();
    }

    public void setLocale(String lang) {
        String languageToLoad = lang; // your language
        if (!lang.equals(getPreferences().getString(RequestParamUtils.LANGUAGE, "en"))) {
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            recreate();
        }
        setScreenLayoutDirection();
        initDrawer();

        SharedPreferences.Editor pre = getPreferences().edit();
        pre.putString(RequestParamUtils.LANGUAGE, lang);
        pre.commit();


//
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        ButterKnife.bind(this);
//        setScreenLayoutDirection();
//        ivDrawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_drawer));
//        // Get token and Save Notification Token
//        String token = FirebaseInstanceId.getInstance().getToken();
//        SharedPreferences.Editor pre = getPreferences().edit();
//        pre.putString(RequestParamUtils.NOTIFICATION_TOKEN, token);
//        pre.putString(RequestParamUtils.LANGUAGE, lang);
//        pre.commit();
//        initDrawer();
//        swipeView();
//        settvImage();
//        showNotification();
//        showCart();
//        setHomeCategoryData();
//        setView();
//        categoryData();
//        verticalBannerData();
//        setMostPopularAdapter();
//        setSpecialOfferAdapter();
//        setSixReasonAdapter();
//        setRecentViewAdapter();
//        getRecentData();
//        if (Constant.IS_RTL) {
//            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.FILL_PARENT);
//            params.gravity = Gravity.END;
//
//            drawerListView.setLayoutParams(params);
//        } else {
//            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.FILL_PARENT);
//            params.gravity = Gravity.START;
//            drawerListView.setLayoutParams(params);
//        }
    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Snackbar.make(llMain, "Press again to exit Application", Snackbar.LENGTH_LONG).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}



