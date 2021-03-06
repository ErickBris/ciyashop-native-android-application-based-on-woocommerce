package com.example.ciyashop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ciyashop.R;
import com.example.ciyashop.activity.ProductDetailActivity;
import com.example.ciyashop.customview.MaterialRatingBar;
import com.example.ciyashop.customview.swipeview.SwipeRevealLayout;
import com.example.ciyashop.customview.swipeview.ViewBinderHelper;
import com.example.ciyashop.customview.textview.TextViewBold;
import com.example.ciyashop.customview.textview.TextViewLight;
import com.example.ciyashop.customview.textview.TextViewRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Cart;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.Debug;
import com.example.ciyashop.utils.RequestParamUtils;
import com.example.ciyashop.utils.URLS;
import com.example.ciyashop.utils.apicall.PostApi;
import com.example.ciyashop.utils.apicall.interfaces.OnResponseListner;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class CartAdapter extends RecyclerView.Adapter {

    private List<Cart> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private DatabaseHelper databaseHelper;
    String value;
    private int isBuynow = 0;


    public CartAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
        databaseHelper = new DatabaseHelper(activity);
        binderHelper.setOpenOnlyOne(true);
    }

    public void addAll(List<Cart> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    public void isFromBuyNow(int isBuynow) {
        this.isBuynow = isBuynow;
    }

    public List<Cart> getList() {
        return list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
        final CartViewHolder holder = (CartViewHolder) h;
        if (list != null && 0 <= position && position < list.size()) {
            // bindview start
            final String data = list.get(position).getCartId() + "";

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipe_layout, position + "");
            binderHelper.closeLayout(position + "");
            holder.bind();
            holder.swipe_layout.close(true);
            if (!list.get(position).getCategoryList().averageRating.equals("")) {
                holder.ratingBar.setRating(Float.parseFloat(list.get(position).getCategoryList().averageRating));
            } else {
                holder.ratingBar.setRating(0);
            }
            if (list.get(position).getCategoryList().images.size() > 0) {
                holder.ivImage.setVisibility(View.VISIBLE);
                Picasso.with(activity).load(list.get(position).getCategoryList().images.get(0).src).into(holder.ivImage);
            } else {
                holder.ivImage.setVisibility(View.INVISIBLE);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            tvProductName.setText(categoryList.name + "");
                holder.tvName.setText(Html.fromHtml(list.get(position).getCategoryList().name + "", Html.FROM_HTML_MODE_LEGACY));
            } else {
//            tvProductName.setText(categoryList.name + "");
                holder.tvName.setText(Html.fromHtml(list.get(position).getCategoryList().name + ""));
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).getCategoryList().priceHtml, Html.FROM_HTML_MODE_COMPACT));

            } else {
                holder.tvPrice.setText(Html.fromHtml(list.get(position).getCategoryList().priceHtml));
            }
            holder.tvPrice.setTextSize(15);

            if (!holder.tvPrice.getText().toString().contains("???") && holder.tvPrice.getText().toString().contains(" ")) {
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
            holder.tvQuantity.setTextColor(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
            holder.tvQuantity.setText(list.get(position).getQuantity() + "");

            holder.tvIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quntity = Integer.parseInt(holder.tvQuantity.getText().toString());
                    quntity = quntity + 1;
                    holder.tvQuantity.setText(quntity + "");
                    databaseHelper.updateQuantity(quntity, list.get(position).getProductid(), list.get(position).getVariationid() + "");
                    list.get(position).setQuantity(quntity);
                    onItemClickListner.onItemClick(position, "increment", quntity);
                }
            });

            holder.tvDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quntity = Integer.parseInt(holder.tvQuantity.getText().toString());
                    quntity = quntity - 1;
                    if (quntity < 1) {
                        quntity = 1;
                    }
                    holder.tvQuantity.setText(quntity + "");
                    databaseHelper.updateQuantity(quntity, list.get(position).getProductid(), list.get(position).getVariationid() + "");
                    list.get(position).setQuantity(quntity);
                    onItemClickListner.onItemClick(position, "decrement", quntity);
                }
            });

            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isBuynow == 0) {
                        if (list.get(position).getCategoryList().type.equals("external")) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getCategoryList().externalUrl));
                            activity.startActivity(browserIntent);
                        } else {
                            Constant.CATEGORYDETAIL = list.get(position).getCategoryList();
                            Intent intent = new Intent(activity, ProductDetailActivity.class);
                            intent.putExtra(RequestParamUtils.ID, list.get(position).getCategoryList().id);
                            activity.startActivity(intent);
                        }

                    }

                }
            });

            try {
                JSONObject jObject = new JSONObject(list.get(position).getVariation());
                Iterator iter = jObject.keys();
                value = "";
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (value.length() == 0) {
                        value = value + key + " : " + jObject.getString(key);
                    } else {
                        value = value + ", " + key + " : " + jObject.getString(key);
                    }
                }
            } catch (Exception e) {
                Log.e("exception is ", e.getMessage());
            }
            holder.txtVariation.setText(value + "");
            //bind view over
        }

    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipe_layout;

        @BindView(R.id.delete_layout)
        View delete_layout;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvPrice)
        TextViewRegular tvPrice;

        @BindView(R.id.tvPrice1)
        TextViewRegular tvPrice1;

        @BindView(R.id.txtVariation)
        TextViewLight txtVariation;

        @BindView(R.id.tvQuantity)
        TextViewBold tvQuantity;

        @BindView(R.id.tvIncrement)
        ImageView tvIncrement;

        @BindView(R.id.tvDecrement)
        ImageView tvDecrement;


        public CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {
            swipe_layout.close(true);
            delete_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swipe_layout.isOpened()) {
                        databaseHelper.deleteFromCart(list.get(getAdapterPosition()).getProductid());
                        list.remove(getAdapterPosition());
                        onItemClickListner.onItemClick(getAdapterPosition(), "delete", 0);
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }


    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.height);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2 - 20;
        height = width - height_value;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}