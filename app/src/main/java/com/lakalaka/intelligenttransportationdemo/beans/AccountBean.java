package com.lakalaka.intelligenttransportationdemo.beans;

import java.util.List;

/**
 * Created by 无情 on 2018/3/18.
 */

public class  AccountBean {

    /**
     * car_money : 550
     * car_number : A10001
     * car_numbers : []
     * car_owner : 张三
     * id : 1
     */



//    这个表是通过所给的一些信息所建，例如上面所给的信息，有什么所用id见什么元素
    private int car_money;
    private String car_number;
    private String car_owner;
    private int id;
    private List<?> car_numbers;
    private int bitmapRes;

    public int getCar_money() {
        return car_money;
    }

    public void setCar_money(int car_money) {
        this.car_money = car_money;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCar_owner() {
        return car_owner;
    }

    public void setCar_owner(String car_owner) {
        this.car_owner = car_owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getCar_numbers() {
        return car_numbers;
    }

    public void setCar_numbers(List<?> car_numbers) {
        this.car_numbers = car_numbers;
    }

    public int getBitmapRes() {
        return bitmapRes;
    }

    public void setBitmapRes(int bitmapRes) {
        this.bitmapRes = bitmapRes;
    }
}
