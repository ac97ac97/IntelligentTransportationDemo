package com.lakalaka.intelligenttransportationdemo.beans;

/**
 * Created by Administrator on 2018/3/20.
 * 实体类 2个站台 每个站台的2辆号码不一样公交车
 */

public class BusInfoBean {
    private int oneBusPerson;
    private int oneBus_OneSite;
    private int oneBus_TwoSite;
    private int twoBusPerson;
    private int twoBus_OneSite;
    private int twoBus_TwoSite;

    public int getOneBusPerson() {
        return oneBusPerson;
    }

    public void setOneBusPerson(int oneBusPerson) {
        this.oneBusPerson = oneBusPerson;
    }

    public int getOneBus_OneSite() {
        return oneBus_OneSite;
    }

    public void setOneBus_OneSite(int oneBus_OneSite) {
        this.oneBus_OneSite = oneBus_OneSite;
    }

    public int getOneBus_TwoSite() {
        return oneBus_TwoSite;
    }

    public void setOneBus_TwoSite(int oneBus_TwoSite) {
        this.oneBus_TwoSite = oneBus_TwoSite;
    }

    public int getTwoBusPerson() {
        return twoBusPerson;
    }

    public void setTwoBusPerson(int twoBusPerson) {
        this.twoBusPerson = twoBusPerson;
    }

    public int getTwoBus_OneSite() {
        return twoBus_OneSite;
    }

    public void setTwoBus_OneSite(int twoBus_OneSite) {
        this.twoBus_OneSite = twoBus_OneSite;
    }

    public int getTwoBus_TwoSite() {
        return twoBus_TwoSite;
    }

    public void setTwoBus_TwoSite(int twoBus_TwoSite) {
        twoBus_TwoSite = twoBus_TwoSite;
    }

    public BusInfoBean() {
    }

    @Override
    public String toString() {
        return "BusInfoBean{" +
                "oneBusPerson=" + oneBusPerson +
                ", oneBus_OneSite=" + oneBus_OneSite +
                ", oneBus_TwoSite=" + oneBus_TwoSite +
                ", twoBusPerson=" + twoBusPerson +
                ", twoBus_OneSite=" + twoBus_OneSite +
                ", TwoBus_TwoSite=" + twoBus_TwoSite +
                '}';
    }
}
