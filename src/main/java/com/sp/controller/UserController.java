package com.sp.controller;

import com.sp.model.User;
import com.sp.service.userService;
import org.framework.core.annotatior.Controller;
import org.framework.inject.annotation.Autowired;
import org.framework.mvc.annotation.RequestMapping;
import org.framework.mvc.annotation.ResponseBody;
import org.framework.mvc.type.RequestMethod;
import org.framework.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    public  final  static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired(value = "")
    public userService userService;

    @RequestMapping(value = "/sel",method = RequestMethod.GET)
    @ResponseBody
    public User  req(){
        //int aa = userService.say(i);
       // logger.info("结果是 = "+aa);
        User user = new User();
        user.setName("ZZ");
        user.setAge(1);
        return user;
    }
}
