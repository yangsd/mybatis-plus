package com.mybatisplus.service;

import com.mybatisplus.mapper.UserMapper;
import com.mybatisplus.model.UserModel;
import com.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author sdyang
 * @create 2017-12-12 16:14
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserModel> implements IUserService {
}
