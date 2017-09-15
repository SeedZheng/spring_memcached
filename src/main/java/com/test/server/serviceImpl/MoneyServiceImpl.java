package com.test.server.serviceImpl;

import com.test.bean.Money;
import com.test.dao.MoneyMapper;
import com.test.server.MoneyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Seed on 2017/9/15.
 */
@Service("moneyService")
public class MoneyServiceImpl implements MoneyService {

    @Resource(name="moneyDao")
    private MoneyMapper mm;

    @Override
    public Money getMoneyByName(String name) {
       Money money= mm.getMoneyByName(name);

        return money;
    }
}
