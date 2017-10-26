package com.skin.thumbsdemo;

import android.text.TextUtils;

/**
 * 字符串工具类
 * Created by wangcheng on 2017/10/25.
 */

public class StringUtil {

    /**
     * 获取绘制文字时跳动的文字位数
     *
     * @param isAdd   文字是增加还是减少
     * @param content 文字
     * @return 绘制文字时跳动的文字位数
     */
    public static int getLastContentLength(boolean isAdd, String content) {
        if (TextUtils.isEmpty(content) || !TextUtils.isDigitsOnly(content)) {
            return 0;
        }
        int resultInt = Integer.parseInt(content);
        if (!isAdd) {
            resultInt++;
        }
        return Integer.toString(getLastNum(resultInt)).length();
    }

    /**
     * 获取绘制文字时跳动的文字位数里之前的数字
     *
     * @param isAdd   文字是增加还是减少
     * @param content 文字
     * @return 之前的文字
     */
    public static String getOldLastContent(boolean isAdd, String content) {
        int lastContentLength = getLastContentLength(isAdd, content);
        int lastContent = Integer.parseInt(content.substring(content.length() - lastContentLength));
        if (isAdd) {
            lastContent--;
        } else {
            lastContent++;
        }
        return Integer.toString(lastContent);
    }

    /**
     * 获取一个Int型数字中末尾的单数字，eg.4-4,21-1,20-20,321-1,320-20,300-300
     *
     * @param num 输入的数字
     * @return 末尾连续为零的一个数字
     */
    private static int getLastNum(int num) {
        if (num % 10 == 0) {
            num = num / 10;
            return getLastNum(num) * 10;
        } else {
            return num % 10;
        }

    }
}
