package com.example.ciyashop.utils.apicall;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ciyashop.R;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
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

public class GetApi {

    public Activity activity;
    public String paramsName;
    public OnResponseListner onResponseListner;
    private boolean isDialogShow = true;

    public GetApi(Activity activity, String paramsName, OnResponseListner onResponseListner) {
        this.activity = activity;
        this.paramsName = paramsName;
        this.onResponseListner = onResponseListner;
    }

    public void setisDialogShow(boolean isDialogShow) {
        this.isDialogShow = isDialogShow;
    }

    public void callGetApi(String jsonString) {
        if (Utils.isInternetConnected(activity)) {
            new getAPiCall().execute(jsonString);

        } else {
            Toast.makeText(activity, R.string.internet_not_working, Toast.LENGTH_SHORT).show();
            onResponseListner.onResponse("", paramsName);
        }
    }

    public class getAPiCall extends AsyncTask<String, String, String> {
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
            try{



            OAuthService service = new ServiceBuilder().provider(OneLeggedApi10.class)
                    .apiKey(URLS.WOOCONSUMERKEY)
                    .apiSecret(URLS.WOOCONSUMERSECRET)
                    .signatureType(SignatureType.QueryString)
                    .debug()
                    /*.scope(SCOPE).*/
                    .build();

            request = new OAuthRequest(Verb.GET, params[0]);
            request.setConnectTimeout(60, TimeUnit.SECONDS);
            service.signRequest(new Token("", ""), request);

            // Now let's go and ask for a protected resource!
            Log.e("scribe", "Now we're going to access a protected resource...");

            try {
                response = request.send();
                if (response.isSuccessful()) {
                    responsebody = response.getBody();
                    Log.e("response", responsebody);
                }

            } catch (Exception e) {
                Log.e("Exception is ", e.getMessage());
            }

            }catch (Exception e) {
                Log.e("Exception is ",e.getMessage());
            }
            return responsebody;
        }


        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.e(paramsName + "Response is ", response.toString());
            onResponseListner.onResponse(response, paramsName);

        }
    }
}
