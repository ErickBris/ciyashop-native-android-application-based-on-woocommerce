package com.example.ciyashop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ciyashop.R;
import com.example.ciyashop.adapter.SearchCategoryAdapter;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCategoryListActivity extends BaseActivity implements OnItemClickListner {

    @BindView(R.id.rvSearchCategory)
    RecyclerView rvSearchCategory;

    private SearchCategoryAdapter searchCategoryAdapter;
    private Bundle bundle;
    private String from;
    public static  int sortPosition;
    private List<Home.AllCategory> list = new ArrayList<>();
    public static String search,sortBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category_list);
        ButterKnife.bind(this);
        setToolbarTheme();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.all_category));
        getIntentData();
        showSearch();
        showCart();
        showBackButton();
        setSerachAdapter();

    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString("from");
            search = bundle.getString(RequestParamUtils.SEARCH);
            sortBy = bundle.getString(RequestParamUtils.ORDER_BY);
            sortPosition = bundle.getInt(RequestParamUtils.POSITION);
        }
    }

// intent.putExtra(RequestParamUtils.ORDER_BY, Constant.getSortList().get(sortAdapter.getSelectedPosition()).getSyntext());
//            intent.putExtra(RequestParamUtils.POSITION,sortPosition);

    public void setSerachAdapter() {
        searchCategoryAdapter = new SearchCategoryAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchCategory.setLayoutManager(mLayoutManager);
        rvSearchCategory.setAdapter(searchCategoryAdapter);
        rvSearchCategory.setNestedScrollingEnabled(false);
        searchCategoryAdapter.setFrom(from);
        for (int i = 0; i < Constant.MAINCATEGORYLIST.size(); i++) {
            if (Constant.MAINCATEGORYLIST.get(i).parent == 0) {
                list.add(Constant.MAINCATEGORYLIST.get(i));
            }
        }
        searchCategoryAdapter.addAll(list);

    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showCart();
    }
}
