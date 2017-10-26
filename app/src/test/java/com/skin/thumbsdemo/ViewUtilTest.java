package com.skin.thumbsdemo;

import org.junit.Test;

/**
 * Created by wangcheng on 2017/10/25.
 */
public class ViewUtilTest {
    @Test
    public void px2dp() throws Exception {
        String content = "130000";
        int length = getLastContentLength(true, content);
        System.out.print(length);
    }


    public int getLastContentLength(boolean isAdd, String content) {
        /*if (TextUtils.isEmpty(content) || !TextUtils.isDigitsOnly(content)) {
            return "";
        }*/
        int resultInt = Integer.parseInt(content);
        if (!isAdd) {
            resultInt++;
        }
        return Integer.toString(getLastNum(resultInt)).length();
    }

    public int getLastNum(int num) {
        if (num % 10 == 0) {
            num = num / 10;
            return getLastNum(num) * 10;
        } else {
            return num % 10;
        }

    }

}