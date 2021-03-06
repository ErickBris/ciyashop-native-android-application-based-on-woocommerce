package com.example.ciyashop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ciyashop.R;
import com.example.ciyashop.activity.ProductDetailActivity;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.CategoryList;
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

public class GroupProductAdapter extends RecyclerView.Adapter<GroupProductAdapter.GroupProductViewHolder> {

    private List<CategoryList> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;

    public GroupProductAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;

    }

    public void addAll(List<CategoryList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<CategoryList> getList() {
        return list;
    }

    @Override
    public GroupProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_product, parent, false);

        return new GroupProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupProductViewHolder holder, final int position) {
        if (position == list.size() - 1) {
            holder.tvDivider.setVisibility(View.GONE);
        } else {
            holder.tvDivider.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(list.get(position).name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml, Html.FROM_HTML_MODE_COMPACT));

        } else {
            holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml));
        }
        holder.tvPrice.setTextSize(15);

        if (!holder.tvPrice.getText().toString().contains("???") && holder.tvPrice.getText().toString().contains(" ")) {
            String[] array = holder.tvPrice.getText().toString().split(" ");
            if (array.length > 1) {
                String firstPrice = array[0];
                String seconfPrice = array[1];

                String htmlText = "<html><font color='#8E8E8E'>" + " " + firstPrice + "</font></html>";
                String htmlText1 = "<html><font color='"+Constant.PRIMARY_COLOR+"'>" + " " + seconfPrice + "</font></html>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tvPrice.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
                    holder.tvPrice1.setText(Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_COMPACT));

                } else {
                    holder.tvPrice.setText(Html.fromHtml(htmlText));
                    holder.tvPrice1.setText(Html.fromHtml(htmlText1));
                }
            }


            holder.tvPrice1.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.CATEGORYDETAIL = list.get(position);
                Intent intent = new Intent(activity, ProductDetailActivity.class);
                intent.putExtra(RequestParamUtils.ID, list.get(position).id);
                activity.startActivity(intent);

            }
        });

        if (list.get(position).images.size() > 0) {
            holder.ivImage.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(list.get(position).images.get(0).src).into(holder.ivImage);
        } else {
            holder.ivImage.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GroupProductViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvPrice)
        TextViewRegular tvPrice;

        @BindView(R.id.tvPrice1)
        TextViewRegular tvPrice1;

        @BindView(R.id.tvDivider)
        TextViewRegular tvDivider;

        public GroupProductViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}