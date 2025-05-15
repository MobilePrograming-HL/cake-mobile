package nix.cake.android.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayUtils {

    private DisplayUtils(){
        //do not initial me
    }

    public static int px2dp(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dp2px(float dipValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void showSoftInput(Context context) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 显示软键盘
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 显示软键盘
        imm.showSoftInput(view, 0);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager immHide =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 隐藏软键盘
        immHide.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static String convertDoubleTwoDecimalsHasCurrency(Double value) {
        return String.format("đ %,d", value.longValue());
    }
    public static String formatLongToShortString(long value) {
        if (value < 1000) {
            return String.valueOf(value);
        } else if (value < 1000000) {
            return String.format("%dk", value / 1000);
        } else if (value < 1000000000) {
            return String.format("%dm", value / 1000000);
        } else {
            return String.format("%db", value / 1000000000);
        }
    }
    public static String formatIsoToLocal(String isoString) {
        ZonedDateTime utcTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            utcTime = ZonedDateTime.parse(isoString);
        }
        ZonedDateTime localTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
        }
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return localTime.format(formatter);
        }

        return isoString;
    }
}
