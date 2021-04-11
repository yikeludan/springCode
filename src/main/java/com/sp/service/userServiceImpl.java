package com.sp.service;

import com.sp.controller.UserController;
import org.framework.core.annotatior.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class userServiceImpl implements userService {
    public  final  static Logger logger = LoggerFactory.getLogger(userServiceImpl.class);

    @Override
    public int say(int i) {
        logger.info("进入 sevice");
        return i;
    }
}
