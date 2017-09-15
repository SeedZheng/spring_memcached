package com.test.server;

import com.test.bean.User;

public interface IUserServer  
{  
    public User testMethod(String userName);
    
    public void clearAllCache();
    
    public void cacheList();

    public void fluahAll();

    public User queryUser(String name);

    void upUser(String name, String gender);

    void testTrans(int id1, int id2, double money);
}  