package com.example.ciyashop.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class SixReasonAdapter extends RecyclerView.Adapter<SixReasonAdapter.SpecialOfferViewHolder> {

    private List<Home.FeatureBox> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;

    public SixReasonAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<Home.FeatureBox> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    @Override
    public SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_six_reason, parent, false);

        return new SpecialOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialOfferViewHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
//        holder.llMain.getLayoutParams().height = height;
        if (position % 2 == 0) {
            holder.tvRightMargin.setVisibility(View.VISIBLE);
        } else if (position % 2 == 1) {
            holder.tvRightMargin.setVisibility(View.GONE);
        }

        if (list.size() % 2 == 1) {
            if (position == list.size() - 1) {
                holder.tvDivider.setVisibility(View.GONE);
                holder.tvMargin.setVisibility(View.VISIBLE);
            }else {
                    holder.tvDivider.setVisibility(View.VISIBLE);
                holder.tvMargin.setVisibility(View.GONE);
            }
        } else {
            if (position >= list.size() - 2) {
                holder.tvDivider.setVisibility(View.GONE);
                holder.tvMargin.setVisibility(View.VISIBLE);
            }else {
                holder.tvDivider.setVisibility(View.VISIBLE);
                holder.tvMargin.setVisibility(View.GONE);
            }
        }
        holder.ivDotImage.setColorFilter(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        Picasso.with(activity).load(list.get(position).featureImage).into(holder.ivImage);
//        holder.tvSixResonTitle.setText(list.get(position).featureBoxHeading);
        holder.tvDescription.setText(list.get(position).featureContent);
        holder.tvName.setText(list.get(position).featureTitle);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SpecialOfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ivDotImage)
        ImageView ivDotImage;

        @BindView(R.id.tvRightMargin)
        TextViewLight tvRightMargin;

        @BindView(R.id.tvDivider)
        TextViewLight tvDivider;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvDescription)
        TextViewLight tvDescription;

//        @BindViews(R.id.tvSixResonTitle)
//        TextViewBold tvSixResonTitle;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvMargin)
        TextView tvMargin;


        public SpecialOfferViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.five);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2 - 15;
        height = width / 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}