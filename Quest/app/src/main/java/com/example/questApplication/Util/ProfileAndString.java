package com.example.questApplication.Util;

import com.example.questApplication.R;

// 将我们设定的图像与设定的对应String字符串进行转换
public class ProfileAndString {
    public static String DrawableToString(int resId) {
        String result = "2";
        switch (resId) {
            case R.drawable.icon_cat:
                result = "1";
                break;
            case R.drawable.icon_bee:
                result = "2";
                break;
            case R.drawable.icon_duck:
                result = "3";
                break;
            case R.drawable.icon_food:
                result = "4";
                break;
            case R.drawable.icon_lattice:
                result = "5";
                break;
            case R.drawable.icon_rabbit:
                result = "6";
                break;
        }
        return result;
    }

    public static int StringToDrawable(String result) {
        int id = R.drawable.icon_cat;
        switch (result) {
            case "1":
                id = R.drawable.icon_cat;
                break;
            case "2":
                id = R.drawable.icon_bee;
                break;
            case "3":
                id = R.drawable.icon_duck;
                break;
            case "4":
                id = R.drawable.icon_food;
                break;
            case "5":
                id = R.drawable.icon_lattice;
                break;
            case "6":
                id = R.drawable.icon_rabbit;
                break;
        }
        return id;
    }
}
