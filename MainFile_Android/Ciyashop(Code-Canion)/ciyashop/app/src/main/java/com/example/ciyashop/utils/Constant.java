package com.example.ciyashop.utils;

import android.app.Activity;
import android.util.Log;

import com.example.ciyashop.R;
import com.example.ciyashop.model.CategoryList;
import com.example.ciyashop.model.Home;
import com.example.ciyashop.model.Orders;
import com.example.ciyashop.model.Sort;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bhumi Shah on 11/21/2017.
 */

public class Constant {

    public static final String MyPREFERENCES = "com.example.ciyashop";  //TODO:Set your Package name here

    public static final String PACKAGE_NAME = "com.example.ciyashop";  //TODO:Set your Package name here

    public static final String DEVICE_TYPE = "2";

    public static final int DISTANCERANGE = 500;

    public static String INFO_PAGE_DATA = "";

    public static boolean IS_WISH_LIST_ACTIVE = false;

    public static boolean IS_CURRENCY_SWITCHER_ACTIVE = false;

    public static boolean IS_ORDER_TRACKING_ACTIVE = false;

    public static boolean IS_REWARD_POINT_ACTIVE = false;

    public static boolean IS_RTL = false;

    public static int Decimal = 2;

    public static String SECONDARY_COLOR = "#0000CD";

    public static String THOUSANDSSEPRETER = ",";

    public static String DECIMALSEPRETER = ".";

    public static boolean IS_CURRENCY_SET = false;

    public static List<Home.AllCategory> MAINCATEGORYLIST = new ArrayList<>();

    public static CategoryList CATEGORYDETAIL = new CategoryList();

    public static Orders ORDERDETAIL = new Orders();

    public static String CURRENCYSYMBOL;

    public static String CURRENCYSYMBOLPOSTION = "left";

    public static Home.PgsAppSocialLinks SOCIALLINK;

    public static final String PRIMARY_COLOR = "#04d39f";

    public static String APPLOGO = "";

    public static String APPLOGO_LIGHT = "";

    public static String DEVICE_TOKEN = "";

    public static String APP_COLOR = "primary_color";

    public static String SECOND_COLOR = "secondary_color";

    public static String HEADER_COLOR = "header_color";

    public static String HEAD_COLOR = "#04d39f";

    public static String ADDRESS_LINE1 = "address_line1";

    public static String ADDRESS_LINE2 = "address_line2";

    public static String PHONE = "phone_number";

    public static String EMAIL = "email_address";

    public static String RTL = "is_rtl";

    public static float LAT;

    public static float LNG;

    public static String APP_TRANSPARENT = "colorPrimaryTransperent";

    public static String APP_TRANSPARENT_VERY_LIGHT = "colorPrimaryVeryLight";

    public static List<String> CurrencyList = new ArrayList<>();

    public static List<Sort> getSortList(Activity activity) {
        List<Sort> sortList = new ArrayList<>();
        if (sortList.size() == 0) {
            sortList.add(new Sort(activity.getString(R.string.newest_first), 0, ""));
            sortList.add(new Sort(activity.getString(R.string.rating), 1, "rating"));
            sortList.add(new Sort(activity.getString(R.string.popularity), 2, "popularity"));
            sortList.add(new Sort(activity.getString(R.string.low_to_high), 3, "price"));
            sortList.add(new Sort(activity.getString(R.string.high_to_low), 4, "price-desc"));
        }
        return sortList;
    }

    public static String setDecimalTwo(Double digit) {
        return new DecimalFormat("##.##").format(digit);
    }

    public static String setDecimal(Double digit) {

        String decimal = "#,##0.";

        if (Constant.Decimal == 0) {
            decimal = "#,##0";
        }
        if ((digit == Math.floor(digit)) && !Double.isInfinite(digit)) {
            // integer type
            for (int i = 0; i < Constant.Decimal; i++) {
                decimal = decimal + "0";
            }
        } else {
            for (int i = 0; i < Constant.Decimal; i++) {
                decimal = decimal + "#";
            }
        }


        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.US);
        if (Constant.Decimal != 0) {
            unusualSymbols.setDecimalSeparator((char) Constant.DECIMALSEPRETER.charAt(0));
        }
        unusualSymbols.setGroupingSeparator(Constant.THOUSANDSSEPRETER.charAt(0));

//        String strange = "#,##0.000";
        DecimalFormat weirdFormatter = new DecimalFormat(decimal, unusualSymbols);

        weirdFormatter.setGroupingSize(3);

        String data = weirdFormatter.format(digit);
        Log.e("data is ", data + "");


        return data;
    }

    public static String setDate(String str) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(str);
            String formattedDate = targetFormat.format(date);  // 20120821
            return formattedDate;
        } catch (ParseException e) {
            Log.e("error", e.getMessage());
        }
        return null;
    }


}
