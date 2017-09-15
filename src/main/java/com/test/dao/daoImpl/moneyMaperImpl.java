package com.test.dao.daoImpl;

import com.test.bean.Money;
import com.test.dao.MoneyMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Seed on 2017/9/15.
 */
@Repository("moneyDao")
public class moneyMaperImpl implements MoneyMapper {

    @Resource(name = "sqlSession")
    private SqlSessionTemplate sqlSession;



    @Override
    public Money getMoneyByName(String name) {
        Money money=sqlSession.selectOne("MoneyMapper.getDataByName",name);
        return money;
    }
}
