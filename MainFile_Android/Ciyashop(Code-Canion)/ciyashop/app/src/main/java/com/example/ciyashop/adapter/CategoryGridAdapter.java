package com.example.ciyashop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.google.gson.Gson;
import com.example.ciyashop.R;
import com.example.ciyashop.activity.ProductDetailActivity;
import com.example.ciyashop.customview.MaterialRatingBar;
import com.example.ciyashop.customview.like.animation.SparkButton;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.CategoryList;
import com.example.ciyashop.model.WishList;
import com.example.ciyashop.utils.BaseActivity;
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

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.CategoryGridHolder> {

    private List<CategoryList> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private DatabaseHelper databaseHelper;


    public CategoryGridAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
        databaseHelper = new DatabaseHelper(activity);
    }

//    public void addAll(List<CategoryList> list) {
//        int index = this.list.size();
//        this.list.addAll(list);
//        notifyItemRangeInserted(index, list.size());
////        notifyDataSetChanged();
//    }

    public void addAll(List<CategoryList> list) {
        for (CategoryList item : list) {
            add(item);
        }
//        this.list = list;
//        notifyDataSetChanged();
    }

    public void add(CategoryList item) {
        int prevSize = list.size();
        this.list.add(item);
        if (list.size() > 1) {
            notifyItemInserted(list.size() - 1);
        } else {
            notifyDataSetChanged();
        }

    }

    public void newList() {
        this.list = new ArrayList<>();
    }


    @Override
    public CategoryGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_category, parent, false);

        return new CategoryGridHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGridHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).type.equals("external")) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).externalUrl));
                    activity.startActivity(browserIntent);
                } else {
                    Constant.CATEGORYDETAIL = list.get(position);
                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                    activity.startActivity(intent);
                }

            }
        });

        if (!list.get(position).averageRating.equals("")) {
            holder.ratingBar.setRating(Float.parseFloat(list.get(position).averageRating));
        } else {
            holder.ratingBar.setRating(0);
        }

//        Picasso.with(activity)
//                .load(list.get(position).appthumbnail)
//                .noFade()
//                .into(holder.ivImage);

        Glide.with(activity)
                .load(list.get(position).appthumbnail)
                .asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.ivImage);


        holder.tvName.setText(list.get(position).name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvPrice.setText(Html.fromHtml(list.get(position).priceHtml));
        }
        holder.tvPrice.setTextSize(15);

        if (!holder.tvPrice.getText().toString().contains("–") && holder.tvPrice.getText().toString().contains(" ")) {
            String[] array = holder.tvPrice.getText().toString().split(" ");
            if (array.length > 1) {
                String firstPrice = array[0];
                String seconfPrice = array[1];

                String htmlText = "<html><font color='#8E8E8E'>" + " " + firstPrice + "</font></html>";
                String htmlText1 = "<html><font color='" + ((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR) + "'>" + " " + seconfPrice + "</font></html>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tvPrice.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
                    holder.tvPrice1.setText(Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_COMPACT));

                } else {
                    holder.tvPrice.setText(Html.fromHtml(htmlText));
                    holder.tvPrice1.setText(Html.fromHtml(htmlText1));
                }
            }
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPrice1.setText(holder.tvPrice.getText().toString());
            holder.tvPrice1.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
            holder.tvPrice.setText("");
        }

        holder.ivWishList.setActivetint(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        holder.ivWishList.setColors(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));


        if (Constant.IS_WISH_LIST_ACTIVE) {
            holder.ivWishList.setVisibility(View.VISIBLE);

            if (databaseHelper.getWishlistProduct(list.get(position).id + "")) {
                holder.ivWishList.setChecked(true);
            } else {
                holder.ivWishList.setChecked(false);
            }
        } else {
            holder.ivWishList.setVisibility(View.GONE);
        }


        holder.ivWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHelper.getWishlistProduct(list.get(position).id + "")) {
                    holder.ivWishList.setChecked(false);
                    onItemClickListner.onItemClick(list.get(position).id, "delete", 0);
                    databaseHelper.deleteFromWishList(list.get(position).id + "");
                } else {
                    holder.ivWishList.setChecked(true);
                    holder.ivWishList.playAnimation();
                    WishList wishList = new WishList();
                    wishList.setProduct(new Gson().toJson(list.get(position)));
                    wishList.setProductid(list.get(position).id + "");
                    databaseHelper.addToWishList(wishList);
                    onItemClickListner.onItemClick(list.get(position).id, "insert", 0);
                }
            }
        });

        ViewTreeObserver vto = holder.ivImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                holder.ivImage.getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("Height: " + holder.ivImage.getMeasuredHeight(), " Width: " + holder.ivImage.getMeasuredWidth());
                return true;
            }
        });

    }

    @Override
    public void onViewRecycled(CategoryGridHolder holder) {
        super.onViewRecycled(holder);

        Picasso.with(holder.itemView.getContext())
                .cancelRequest(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryGridHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.ivWishList)
        SparkButton ivWishList;

        @BindView(R.id.tvName)
        TextViewLight tvName;

        @BindView(R.id.tvPrice)
        TextViewRegular tvPrice;

        @BindView(R.id.tvPrice1)
        TextViewRegular tvPrice1;


        public CategoryGridHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}