package com.test.dao;

import com.test.bean.User;

public interface UserMapper
{
    //@Select("select * from user where name=#{name}")
    public User getUser(String name);

    int addMoney(int id, double money);

    int subMoney(int id, double money);

    void upUser(String name, String gender);



}  