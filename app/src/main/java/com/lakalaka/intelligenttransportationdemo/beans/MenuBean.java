package com.lakalaka.intelligenttransportationdemo.beans;

/**
 * Created by Administrator on 2018/3/18.
 */

public class MenuBean {
    int icon;
    String title;

    public MenuBean() {
    }

    public MenuBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
