package com.example.ciyashop.activity;

import com.example.ciyashop.R;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.model.Cart;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebviewActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.wvCheckOut)
    WebView wvCheckOut;

    @BindView(R.id.wvCheckOut1)
    WebView wvCheckOut1;


    @BindView(R.id.ivBack)
    ImageView ivBack;

    String url, thank_you_url, home_url , track_url;
    private DatabaseHelper databaseHelper;
    private boolean isfirstLoad = false;
    private int buyNow;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        databaseHelper = new DatabaseHelper(this);
        ButterKnife.bind(this);
        settvImage();
        hideSearchNotification();
        setToolbarTheme();
        showBackButton();
        url = getIntent().getExtras().getString(RequestParamUtils.CHECKOUT_URL);
        thank_you_url = getIntent().getExtras().getString(RequestParamUtils.THANKYOU);
        home_url = getIntent().getExtras().getString(RequestParamUtils.HOME_URL);
        buyNow = getIntent().getExtras().getInt("buynow");
        wvCheckOut.getSettings().setLoadsImagesAutomatically(true);
        wvCheckOut.getSettings().setJavaScriptEnabled(true);
        wvCheckOut.addJavascriptInterface(new WebAppInterface(this), "Android");
        wvCheckOut.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvCheckOut, true);
        }

        wvCheckOut.setWebViewClient(wvc);
        CookieManager.getInstance().setAcceptCookie(true);

        try {

            JSONObject jsonObject = new JSONObject();
            String customerId = getPreferences().getString(RequestParamUtils.ID, "");

            jsonObject.put("user_id", customerId);
            jsonObject.put("cart_items", getCartDataForAPI());
            jsonObject.put("os", "android");
            Log.e("Lopa ", jsonObject.toString());
            String postData = jsonObject.toString();
            wvCheckOut.postUrl(url, postData.getBytes());
            showProgress("");
            setToolbarTheme();
            wvCheckOut.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
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


    @OnClick(R.id.ivBack)
    public void ivBackClick() {
        if (wvCheckOut.canGoBack()) {
            wvCheckOut.goBack();
        } else {
            CookieManager.getInstance().removeAllCookie();
            wvCheckOut.clearCache(true);
            wvCheckOut.clearHistory();
            clearCookies(WebviewActivity.this);
            logout();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvCheckOut.canGoBack()) {
            wvCheckOut.goBack();
            return true;
        } else {
            CookieManager.getInstance().removeAllCookie();
            wvCheckOut.clearCache(true);
            wvCheckOut.clearHistory();
            clearCookies(WebviewActivity.this);
            logout();
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebViewClient wvc = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("lopa", "Url called");

            if (url.contains(thank_you_url)) {
                if (isfirstLoad) {
                    Intent intent = new Intent(WebviewActivity.this, ThankYouActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    wvCheckOut.clearCache(true);
                    wvCheckOut.clearHistory();
                    clearCookies(WebviewActivity.this);
                    isfirstLoad = false;
                } else {
                    Log.e("Else Condition ","Called");
                }
            } else if (home_url!=null && url.contains(home_url)) {
                Toast.makeText(WebviewActivity.this,"Something went wrong ...try after Somnetime or Contact Admin",Toast.LENGTH_LONG).show();
            }
            return false;
        }
    };

    public void logout() {

        PostApi postApi = new PostApi(this, "logout", this);
        postApi.callPostApi(new URLS().LOGOUT, "");
    }


    @Override
    public void onResponse(final String response, String methodName) {
        dismissProgress();
        if (methodName.equals("logout")) {


            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        finish();
                    } else {

                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }


    @SuppressWarnings("deprecation")
    public void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.e("log", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.e("log", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }


    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(final String toast) {
            Log.e("Title is ", toast);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgress();
                    if (toast != null) {
                        CookieManager.getInstance().setAcceptCookie(true);
                        wvCheckOut.loadUrl(toast);
                        wvCheckOut.setVisibility(View.VISIBLE);
                        isfirstLoad = true;
                        dismissProgress();
                    }
                }
            });

        }
    }


}
