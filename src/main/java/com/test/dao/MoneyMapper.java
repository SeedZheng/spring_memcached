package com.test.dao;

import com.test.bean.Money;

/**
 * Created by Seed on 2017/9/15.
 */
public interface MoneyMapper {

    Money getMoneyByName(String name);
}
