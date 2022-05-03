 package com.example.ciyashop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.ciyashop.R;
import com.example.ciyashop.adapter.SearchHomeAdapter;
import com.example.ciyashop.customview.edittext.EditTextRegular;
import com.example.ciyashop.helper.DatabaseHelper;
import com.example.ciyashop.interfaces.OnItemClickListner;
import com.example.ciyashop.utils.BaseActivity;
import com.example.ciyashop.utils.Constant;
import com.example.ciyashop.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFromHomeActivity extends BaseActivity implements OnItemClickListner {

    @BindView(R.id.etSearch)
    EditTextRegular etSearch;

    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;
    private SearchHomeAdapter searchHomeAdapter;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_from_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        }
        setToolbarTheme();
        databaseHelper = new DatabaseHelper(SearchFromHomeActivity.this);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        showBackButton();
        searchClick();
        setFilter();
        setSearchAdapter();

    }

    public void setFilter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


    }


    public void filter(String text) {
        List<String> temp = new ArrayList();
        for (String d : databaseHelper.getSearchHistoryList()) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        searchHomeAdapter.updateList(temp);
    }


    public void setSearchAdapter() {
        searchHomeAdapter = new SearchHomeAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearch.setLayoutManager(mLayoutManager);
        rvSearch.setAdapter(searchHomeAdapter);
        rvSearch.setNestedScrollingEnabled(false);
        if (databaseHelper.getSearchHistoryList() != null) {
            searchHomeAdapter.addAll(databaseHelper.getSearchHistoryList());
        }

    }


    public void searchClick() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch();
                    Intent intent = new Intent(SearchFromHomeActivity.this, CategoryListActivity.class);
                    intent.putExtra(RequestParamUtils.SEARCH, etSearch.getText().toString());
                    startActivity(intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!databaseHelper.getSearchItem(etSearch.getText().toString())) {
                                databaseHelper.addToSearchHistory(etSearch.getText().toString());
                            }

                            if (databaseHelper.getSearchHistoryList() != null) {
                                searchHomeAdapter.addAll(databaseHelper.getSearchHistoryList());
                            }
                            etSearch.setText("");
                        }
                    }, 200);


                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onItemClick(int position, String value, int outerPos) {

    }


}
