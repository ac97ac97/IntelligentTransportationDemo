package com.lakalaka.intelligenttransportationdemo.adapter;

/**
 * Created by Administrator on 2018/3/28.
 */

public class LeftMenu {
    private int leftMenuImg;
    private String leftMenuText;

    public LeftMenu(int leftMenuImg, String leftMenuText) {
        this.leftMenuImg = leftMenuImg;
        this.leftMenuText = leftMenuText;
    }

    public int getLeftMenuImg() {
        return leftMenuImg;
    }

    public void setLeftMenuImg(int leftMenuImg) {
        this.leftMenuImg = leftMenuImg;
    }

    public String getLeftMenuText() {
        return leftMenuText;
    }

    public void setLeftMenuText(String leftMenuText) {
        this.leftMenuText = leftMenuText;
    }
}
