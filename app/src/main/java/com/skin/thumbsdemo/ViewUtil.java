package com.skin.thumbsdemo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * SP,DP和PX转换工具类
 * Created by wangcheng on 2017/10/24.
 */
public class ViewUtil {

    /**
     * PX转化为DP
     *
     * @param context 上下文
     * @param px      像素
     * @return DP值
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((px * 160) / scale + 0.5f);
    }

    /**
     * DP转化为PX
     *
     * @param context 上下文
     * @param dp      DP值
     * @return 像素值
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (dp * (scale / 160) + 0.5f);
    }

    /**
     * SP转PX
     *
     * @param context 上下文
     * @param val     SP值
     * @return 像素值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * PX转SP
     *
     * @param fontScale 上下文
     * @param pxVal     像素值
     * @return SP值
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStateBarHeight(Context context) {
        int statusBarHeight = dp2px(context, 15);
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕尺寸信息
     *
     * @param context 上下文
     * @return 屏幕尺寸信息metrics，屏幕宽为metrics.widthPixels, 屏幕高为metrics.heightPixels
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
