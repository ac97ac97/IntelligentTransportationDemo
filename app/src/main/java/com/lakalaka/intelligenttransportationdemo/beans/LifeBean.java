package com.lakalaka.intelligenttransportationdemo.beans;

/**
 * Created by lakalaka on 2018/3/20/0020.
 */

public class LifeBean {

    private int icon;
    private String jibie;
    private String tishiInfo;

    public LifeBean() {

    }

    public LifeBean(int icon, String jibie, String tishiInfo) {
        this.icon = icon;
        this.jibie = jibie;
        this.tishiInfo = tishiInfo;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getJibie() {
        return jibie;
    }

    public void setJibie(String jibie) {
        this.jibie = jibie;
    }

    public String getTishiInfo() {
        return tishiInfo;
    }

    public void setTishiInfo(String tishiInfo) {
        this.tishiInfo = tishiInfo;
    }

    @Override
    public String toString() {
        return "LifeBean{" +
                "icon=" + icon +
                ", jibie='" + jibie + '\'' +
                ", tishiInfo='" + tishiInfo + '\'' +
                '}';
    }
}
