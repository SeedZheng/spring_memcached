package com.test.web;

import com.test.bean.Money;
import com.test.bean.User;
import com.test.server.IUserServer;
import com.test.server.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller  
//@RequestMapping("/loginController")
public class LoginController  
{  
	@Resource(name="server")
    IUserServer server;
	@Resource(name = "moneyService")
    private MoneyService ms;
    


    @RequestMapping("/login.json")  
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mode = new ModelAndView("login");
        User user=server.queryUser("aa");

        mode.addObject("user", user);  
        return mode;  
    }

    @RequestMapping("/login2.json")
    public ModelAndView login2(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mode = new ModelAndView("login");
        User user=server.queryUser("bb");

        mode.addObject("user", user);
        return mode;
    }

    @RequestMapping("/getMoney.json")
    @ResponseBody
    public void getMoney(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Money money=ms.getMoneyByName("陈一");
        System.out.println(money);

    }

    @RequestMapping("/update.json")
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        server.upUser("aa","aaa");
       // return mode;
    }


    @RequestMapping("/testTrans.json")
    public void testTrans(){
        server.testTrans(1,2,100);
    }

}  