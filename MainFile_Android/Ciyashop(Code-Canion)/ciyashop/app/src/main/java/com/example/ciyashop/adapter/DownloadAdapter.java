package com.example.ciyashop.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ciyashop.R;
import com.example.ciyashop.customview.scratchview.ScratchTextView;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewMedium;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.javaclasses.FilterSelectedList;
import com.example.ciyashop.model.Download;
import com.example.ciyashop.model.Rewards;
import com.example.ciyashop.model.Success;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by User on 16-11-2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.RecentViewHolder> {
    private List<Download> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;

    public DownloadAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }


    public void addAll(List<Download> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<Download> getList() {
        return this.list;
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download, parent, false);

        return new RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentViewHolder holder, final int position) {

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //full view click
            }
        });
        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open url in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).downloadUrl));
                activity.startActivity(browserIntent);
//                new DownloadFileFromURL().execute(list.get(position).downloadUrl);

                String urlString = list.get(position).downloadUrl;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    activity.startActivity(intent);
                }
            }
        });
//        holder.ivDownload.getDrawable().setColorFilter(new
//                PorterDuffColorFilter(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), PorterDuff.Mode.OVERLAY));
        holder.ivDownload.setBackgroundColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        try {
            String[] date = list.get(position).accessExpires.split("T");
            holder.tvExpiration.setText(" " + date[0] + "");


        } catch (Exception e) {
            Log.e("Exception is ", e.getMessage());
            holder.tvExpiration.setText(" " + list.get(position).accessExpires + "");
        }


        holder.tvFileName.setText(list.get(position).downloadName + "");
        holder.tvTitle.setText(list.get(position).productName + "");
        holder.tvRemain.setText(" " + list.get(position).downloadsRemaining + "");

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class RecentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.tvBottom)
        TextView tvBottom;

        @BindView(R.id.tvTitle)
        TextViewMedium tvTitle;

        @BindView(R.id.tvRemain)
        TextViewRegular tvRemain;

        @BindView(R.id.tvExpiration)
        TextViewRegular tvExpiration;

        @BindView(R.id.ivDownload)
        ImageView ivDownload;

        @BindView(R.id.tvFileName)
        TextViewRegular tvFileName;


        public RecentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
