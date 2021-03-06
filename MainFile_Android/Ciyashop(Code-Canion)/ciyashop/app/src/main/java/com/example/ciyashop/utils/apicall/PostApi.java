package com.example.ciyashop.utils.apicall;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ciyashop.R;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.Debug;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.Utils;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.concurrent.TimeUnit;

/**
 * Created by Bhumi Shah on 11/21/2017.
 */

public class PostApi {

    public Activity activity;
    public String paramsName;
    public OnResponseListner onResponseListner;
    private boolean isDialogShow = true;

    public PostApi(Activity activity, String paramsName, OnResponseListner onResponseListner) {
        this.activity = activity;
        this.paramsName = paramsName;
        this.onResponseListner = onResponseListner;
    }

    public void setisDialogShow(boolean isDialogShow) {
        this.isDialogShow = isDialogShow;
    }

    public void callPostApi(String urls, String json) {
        if (Utils.isInternetConnected(activity)) {
            Log.e("callPostApi", json);
            new postAPiCall().execute(urls, json);
        } else {
            Toast.makeText(activity, R.string.internet_not_working, Toast.LENGTH_SHORT).show();
            onResponseListner.onResponse("", paramsName);
        }
    }

    public class postAPiCall extends AsyncTask<String, String, String> {
        Response response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isDialogShow)
                ((BaseActivity) activity).showProgress("Loading .. ");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e(paramsName, params[0]);

            OAuthRequest request;
            String responsebody = "";
            try {
                OAuthService service;


                if (params[0].contains(new URLS().WOO_MAIN_URL)) {
                    service = new ServiceBuilder().provider(OneLeggedApi10.class)
                            .apiKey(URLS.WOOCONSUMERKEY)
                            .apiSecret(URLS.WOOCONSUMERSECRET)
                            .signatureType(SignatureType.QueryString)
                            .debug()
                            /*.scope(SCOPE).*/
                            .build();
                } else {
                    service = new ServiceBuilder().provider(OneLeggedApi10.class)
                            .apiKey(URLS.CONSUMERKEY)
                            .apiSecret(URLS.CONSUMERSECRET)
                            .signatureType(SignatureType.QueryString)
                            .debug()
                            /*.scope(SCOPE).*/
                            .build();
                }
                request = new OAuthRequest(Verb.POST, params[0]);

                String parameters = params[1];
                Log.e("p", parameters);
                request.addHeader("Content-Type", "application/json");
//            request.addHeader("Authorization",  service.getAuthorizationUrl(new Token(Constant.OAUTH_TOKEN, Constant.OAUTH_TOKEN_SECRET)));

                if (params[0].contains(new URLS().WOO_MAIN_URL)) {
                    service.signRequest(new Token("", ""), request);
                } else {
                    service.signRequest(new Token(URLS.OAUTH_TOKEN, URLS.OAUTH_TOKEN_SECRET), request);
                }
                request.addPayload(parameters);
                request.setConnectTimeout(60, TimeUnit.SECONDS);

                // Now let's go and ask for a protected resource!

                try {
                    response = request.send();
                    if (response.isSuccessful()) {
                        responsebody = response.getBody();
                        Log.e("response", responsebody);
                    }
                } catch (Exception e) {
                    Log.e("Exception is ", e.getMessage());
                    return "OAuthConnectionException";
                }
            } catch (Exception e) {
                Log.e("Exception is ", e.getMessage());
            }

            return responsebody;
        }

        @Override
        protected void onPostExecute(final String response) {
            super.onPostExecute(response);
            Debug.e(paramsName + "Response is ", response.toString());
            onResponseListner.onResponse(response, paramsName);

        }
    }
}
