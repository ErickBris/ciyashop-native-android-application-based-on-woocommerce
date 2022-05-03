package com.example.ciyashop.javaclasses;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.model.SyncWishListModel;
import com.example.ciyashop.model.WishList;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Bhumi Shah on 12/7/2017.
 */

public class SyncWishList implements OnResponseListner {

    private Activity context;
    private DatabaseHelper databaseHelper;

    public SyncWishList(Activity context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public String getWishListData(String userid) {
        JSONObject jsonObject = new JSONObject();
        List<String> localWishListData = databaseHelper.getWishList();

        if (localWishListData != null) {
            try {

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < localWishListData.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put("product_id", localWishListData.get(i));
                    object.put("user_id", userid);
                    object.put("quantity", 1);
                    object.put("wishlist_name", "");
                    jsonArray.put(object);
                }
                jsonObject.put("sync_list", jsonArray);
                jsonObject.put("user_id", userid);
            } catch (Exception e) {
                Log.e("Json Exception", e.getMessage());
            }
            return jsonObject.toString();
        }
        return null;
    }

    public void syncWishList(String userid, boolean isDialogShow) {
        Log.e("lopa ", "get CategoryListDataCalled");
        PostApi postApi = new PostApi(context, "syncWishList", this);
        postApi.setisDialogShow(isDialogShow);
        postApi.callPostApi(new URLS().WISHLIST, getWishListData(userid));
    }

    @Override
    public void onResponse(String response, String methodName) {
        if (methodName.equals("syncWishList")) {
            if (response != null && response.length() > 0) {
                try {
                    SyncWishListModel syncWishListRider = new Gson().fromJson(
                            response, new TypeToken<SyncWishListModel>() {
                            }.getType());

                    for (int i = 0; i < syncWishListRider.syncList.size(); i++) {
                        if (!databaseHelper.getWishlistProduct(syncWishListRider.syncList.get(i).prodId)) {
                            WishList wishList = new WishList();
                            wishList.setProductid(syncWishListRider.syncList.get(i).prodId);
                            databaseHelper.addToWishList(wishList);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }


            }
        }

    }
}
