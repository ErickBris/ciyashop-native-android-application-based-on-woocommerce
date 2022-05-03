package com.example.ciyashop.activity;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThankYouActivity extends BaseActivity implements OnResponseListner {

    private DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        databaseHelper = new DatabaseHelper(this);
        ButterKnife.bind(this);
        setThem();
        databaseHelper.clearCart();
        logout();
    }

    public void logout() {

        PostApi postApi = new PostApi(this, "logout", this);
        postApi.callPostApi(new URLS().LOGOUT, "");
    }

    public void setThem()
    {
        TextViewRegular tvContinueShopping = findViewById(R.id.tvContinueShopping);
        ImageView ivGo = findViewById(R.id.ivGo);
        tvContinueShopping.setTextColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        ivGo.setColorFilter(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setStroke(5, Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
            tvContinueShopping.setBackground(gradientDrawable);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(getPreferences().getString(Constant.HEADER_COLOR, Constant.HEAD_COLOR)));
        }




    }

    @Override
    public void onResponse(final String response, String methodName) {

        if (methodName.equals("logout")) {

            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {

                    } else {

                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ThankYouActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @OnClick(R.id.tvContinueShopping)
    public void tvContinueShoppingClick() {
        Intent i = new Intent(ThankYouActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}