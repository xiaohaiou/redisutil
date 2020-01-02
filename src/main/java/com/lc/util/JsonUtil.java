package com.lc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



public class JsonUtil {

    public static String convertObj2String(Object object) {
        String s = null;
        try {
            s = JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static <T> T convertString2Obj(String s, Class<T> clazz) {
        T t = null;
        try {
            t = JSONObject.parseObject(s,clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
