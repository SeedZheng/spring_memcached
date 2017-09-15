package com.test.server;


import com.test.bean.User;
import com.test.dao.DaoSupport;
import com.test.utils.MemcachedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("server")
public class UserServerImpl implements IUserServer  
{  
	@Autowired
    private DaoSupport userDao;
	
	 MemcachedUtils memcachedUtils;

	@Override
    public User testMethod(String userName)  
    {  
    	//System.out.println(memcachedUtils);
        User user;  
        // 判断缓存中数据是否存在，如果不存在则添加，存在则读取  
        if (this.memcachedUtils.get("user") != null)  
        {  
            user = (User) this.memcachedUtils.get("user");  
            System.out.println("本次操作是在缓存中查询数据...");  
        }  
        else  
        {  
            user = userDao.getUser(userName);  
            this.memcachedUtils.add("user",user,new Date(100));  
            System.out.println("本次操作是在数据库中查询数据...");  
        }  
        return user;  
    }
    @Override
    public User queryUser(){
        User user=userDao.getUser("aa");
        //User user2=userDao.getUser("aa");
        System.out.println(user);
        return user;
    }

    @Override
    public void upUser(String name, String gender) {
        userDao.upUser(name,gender);
    }

    @Override
    @Transactional
    public void testTrans(int id1, int id2, double money) {

       int num= userDao.addMoney(id1,money);
        System.out.println("已给"+num+"个人加了"+money+"元");
        int i=1/0;
       num= userDao.subMoney(id2,money);
        System.out.println("已给"+num+"个人减了"+money+"元");

    }

    @Override
    public void clearAllCache() {
		/*Iterator<Map<String, String>> iterSlabs = memcachedClient
				.getStats("items").values().iterator();
		Set<String> set = new HashSet<String>();
		while (iterSlabs.hasNext()) {
			Map<String, String> slab = iterSlabs.next();
			for (String key : slab.keySet()) {
				String index = key.split(":")[1];
				set.add(index);
			}
		}
		List<String> list = new LinkedList<String>();
		for (String v : set) {
			String commond = "cachedump ".concat(v).concat(" 0");
			Iterator<Map<String, String>> iterItems = memcachedClient
					.getStats(commond).values().iterator();
			while (iterItems.hasNext()) {
				Map<String, String> items = iterItems.next();
				list.addAll(items.keySet());
			}
		}
		for (String key : list) {
			// System.out.println("key:"+key);
			memcachedClient.delete(key);
		}
		*/
	}

    public void cacheList() {

    }

    public void fluahAll() {

    }


}  