package com.example.ciyashop.adapter;


/**
 * Created by Kaushal on 12-12-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciyashop.R;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.SellerData;
import com.example.ciyashop.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class SellerReviewAdapter extends RecyclerView.Adapter<SellerReviewAdapter.ReviewHolder> {

    private List<SellerData.SellerInfo.ReviewList> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;


    public SellerReviewAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
    }

    public void addAll(List<SellerData.SellerInfo.ReviewList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.tvRatting.setText(Constant.setDecimalTwo((double) list.get(position).rating));
        holder.tvName.setText(list.get(position).commentAuthor);
        holder.tvReview.setText(list.get(position).commentContent);
        holder.tvTime.setText(list.get(position).commentDate);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvRatting)
        TextViewRegular tvRatting;

        @BindView(R.id.tvReview)
        TextViewRegular tvReview;

        @BindView(R.id.tvTime)
        TextViewLight tvTime;


        public ReviewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}