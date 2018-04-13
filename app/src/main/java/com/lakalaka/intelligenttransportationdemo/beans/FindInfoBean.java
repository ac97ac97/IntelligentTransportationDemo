package com.lakalaka.intelligenttransportationdemo.beans;

/**
 * Created by Administrator on 2018/3/19.
 */

public class FindInfoBean {
    private int amerce;
    private String car_number;
    private int deduction;
    private int dispose_number;

    public int getAmerce() {
        return amerce;
    }

    public void setAmerce(int amerce) {
        this.amerce = amerce;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public int getDeduction() {
        return deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }

    public int getDispose_number() {
        return dispose_number;
    }

    public void setDispose_number(int dispose_number) {
        this.dispose_number = dispose_number;
    }

    @Override
    public String toString() {
        return "FindInfoBean{" +
                "amerce=" + amerce +
                ", car_number='" + car_number + '\'' +
                ", deduction=" + deduction +
                ", dispose_number=" + dispose_number +
                '}';
    }
}
