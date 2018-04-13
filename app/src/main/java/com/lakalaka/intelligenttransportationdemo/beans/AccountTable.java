package com.lakalaka.intelligenttransportationdemo.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by 无情 on 2018/3/17.
 */


@DatabaseTable(tableName = "accounts")
public class AccountTable {

    @DatabaseField()
    String carId;//车牌号
    @DatabaseField()
    int carMoney;//充值金额
    @DatabaseField()
    int carBalance;//账户余额
    @DatabaseField()
    String accountPerson;//充值人
    @DatabaseField()
    String accountTime;//充值时间
    @DatabaseField()
    String accountWeek;


    /**
     * 这个构造函数一定要写，否则数据库会报错
     */
    public AccountTable() {

    }

    public AccountTable(String carId, int carMoney, int carBalance, String accountPerson,
                        String accountTime, String accountWeek) {

        this.carId = carId;
        this.carMoney = carMoney;
        this.carBalance = carBalance;
        this.accountPerson = accountPerson;
        this.accountTime = accountTime;
        this.accountWeek = accountWeek;
    }


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public int getCarMoney() {
        return carMoney;
    }

    public void setCarMoney(int carMoney) {
        this.carMoney = carMoney;
    }

    public int getCarBalance() {
        return carBalance;
    }

    public void setCarBalance(int carBalance) {
        this.carBalance = carBalance;
    }

    public String getAccountPerson() {
        return accountPerson;
    }

    public void setAccountPerson(String accountPerson) {
        this.accountPerson = accountPerson;
    }

    public String getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(String accountTime) {
        this.accountTime = accountTime;
    }

    public String getAccountWeek() {
        return accountWeek;
    }

    public void setAccountWeek(String accountWeek) {
        this.accountWeek = accountWeek;
    }



}


