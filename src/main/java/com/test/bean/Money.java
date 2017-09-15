package com.test.bean;

import java.io.Serializable;

/**
 * Created by Seed on 2017/9/15.
 */
public class Money implements Serializable {

    private static final long serialVersionUID = -985141821084238350L;

    private int id;
    private String name;
    private double money;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    @Override
    public String toString() {
        return "Money{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
