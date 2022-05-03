package com.example.ciyashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ciyashop.R;
import com.example.ciyashop.activity.CategoryListActivity;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhumi Shah on 11/9/2017.
 */

public class BannerViewPagerAdapter extends PagerAdapter {
    private List<Home.MainSlider> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Activity activity;
    private int length;

    public BannerViewPagerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addAll(List<Home.MainSlider> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_top_banner, container, false);
        final ImageView imageView = view.findViewById(R.id.ivBanner);
        final ProgressBar progress_bar = view.findViewById(R.id.progress_bar);
//        imageView.setImageResource(R.drawable.banner);
        container.addView(view);
        progress_bar.getIndeterminateDrawable().setColorFilter(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), android.graphics.PorterDuff.Mode.MULTIPLY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CategoryListActivity.class);
                intent.putExtra(RequestParamUtils.CATEGORY, list.get(position).sliderCatId);
                intent.putExtra(RequestParamUtils.IS_WISHLIST_ACTIVE, Constant.IS_WISH_LIST_ACTIVE);
                activity.startActivity(intent);

            }
        });

        if (list.get(position).uploadImageUrl == null || list.get(position).uploadImageUrl.equals("")) {
            progress_bar.setVisibility(View.GONE);
        }


        Picasso.with(activity)
                .load(list.get(position).uploadImageUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
//                Log.e("Banner Height: " + imageView.getMeasuredHeight(), "Banner  Width: " +imageView.getMeasuredWidth());
                return true;
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
