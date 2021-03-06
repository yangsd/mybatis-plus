package com.mybatisplus.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by sdyang on 2017/11/9.
 */
public class JsonUtil {

    public static <T> T parseObject(String json,Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    public static String toJSONString(Object object){
        return JSON.toJSONString(object);
    }

    public static <T> List<T> parseArray(String json, Class<T> clasz){return JSON.parseArray(json,clasz);}


}
