package com.example.ciyashop.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.Utils;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactSellerActivity extends BaseActivity implements OnResponseListner {

    @BindView(R.id.etName)
    EditTextRegular etName;

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etMessage)
    EditTextRegular etMessage;

    @BindView(R.id.tvSend)
    TextViewBold tvSend;

    private String sellerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_seller);
        ButterKnife.bind(this);
        setToolbarTheme();
        tvSend.setBackgroundColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));

        settvTitle("CiyaShop Dealer");
        showBackButton();
        sellerid = getIntent().getExtras().getString(RequestParamUtils.ID);
    }

    @OnClick(R.id.tvSend)
    public void tvSendClick() {
        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
        } else {
            if (etEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isValidEmail(etEmail.getText().toString())) {
                    if (etMessage.getText().toString().isEmpty()) {
                        Toast.makeText(this, R.string.enter_message, Toast.LENGTH_SHORT).show();
                    } else {
                        contactSeller();
                    }
                } else {
                    Toast.makeText(this, R.string.enter_valid_email_address, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void contactSeller() {

        PostApi postApi = new PostApi(ContactSellerActivity.this, "contact_seller", this);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", etName.getText().toString());
            jsonObject.put("email", etEmail.getText().toString());
            jsonObject.put("message", etEmail.getText().toString());
            jsonObject.put("seller_id", sellerid);

            postApi.callPostApi(new URLS().CONTACT_SELLER , jsonObject.toString());
        } catch (Exception e) {
            Log.e("Json Exception", e.getMessage());
        }

    }

    @Override
    public void onResponse(String response, String methodName) {
        if (methodName.equals("contact_seller")) {
            dismissProgress();
            if (response != null && response.length() > 0) {
                try {
                    //set call here
                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(this, R.string.message_sent_successfully, Toast.LENGTH_SHORT).show();
                        etName.getText().clear();
                        etEmail.getText().clear();
                        etMessage.getText().clear();
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
