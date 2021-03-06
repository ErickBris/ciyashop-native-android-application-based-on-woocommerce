package com.example.ciyashop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ciyashop.R;
import com.example.ciyashop.activity.CategoryListActivity;
import com.example.ciyashop.activity.SearchCategoryListActivity;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class HomeTopCategoryAdapter extends RecyclerView.Adapter<HomeTopCategoryAdapter.CategoryViewHolder> {

    private List<Home.MainCategory> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;

    public HomeTopCategoryAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<Home.MainCategory> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_top_category, parent, false);


        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
        if (holder.flImage.getLayoutParams().width > width - 10) {
            holder.flImage.getLayoutParams().width = width - 10;
            holder.flImage.getLayoutParams().height = width - 10;
        }

        if (position == list.size() - 1) {
            holder.ivImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_more_white));
        } else {

            if (list.get(position).mainCatImage != null && !list.get(position).mainCatImage .equals("") ) {
                Picasso.with(activity).load(list.get(position).mainCatImage).error(R.drawable.ic_more_white).into(holder.ivImage);
            }
        }
        if (list.get(position).mainCatName != null && !list.get(position).mainCatName .equals("") ) {
            holder.tvName.setText(list.get(position).mainCatName);
        }

        if (position == list.size() - 1) {
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, SearchCategoryListActivity.class);
                    intent.putExtra("from", "search");
                    activity.startActivity(intent);
                }
            });

        } else {
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, CategoryListActivity.class);
                    intent.putExtra(RequestParamUtils.CATEGORY, list.get(position).mainCatId);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.flImage)
        FrameLayout flImage;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}