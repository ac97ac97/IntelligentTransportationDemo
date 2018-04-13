package com.lakalaka.intelligenttransportationdemo.beans;

/**
 * Created by Administrator on 2018/3/19.
 */

public class FaKuanInfoBean {
    private String address;
    private int amerce;
    private int car_id;
    private String date;
    private int deduction;
    private int id;
    private String info;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmerce() {
        return amerce;
    }

    public void setAmerce(int amerce) {
        this.amerce = amerce;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDeduction() {
        return deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FaKuanInfoBean{" +
                "address='" + address + '\'' +
                ", amerce=" + amerce +
                ", car_id=" + car_id +
                ", date='" + date + '\'' +
                ", deduction=" + deduction +
                ", id=" + id +
                ", info='" + info + '\'' +
                '}';
    }
}
