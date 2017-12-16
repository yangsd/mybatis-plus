package com.mybatisplus.controller;

import com.mybatisplus.model.UserModel;
import com.mybatisplus.service.IUserService;
import com.mybatisplus.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sdyang
 * @create 2017-12-12 16:22
 **/
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/test",method = {RequestMethod.GET})
    @ResponseBody
    public String index(Model model) {
       UserModel user = userService.selectById(1);
        return JsonUtil.toJSONString(user);
    }
}
