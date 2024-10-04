package com.tangxianglin.blog.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;


/**
 * JSON 工具类
 * @author Administrator
 *
 */
public class JSONUtil {
    private static ObjectMapper objectMapper = null;
    static {
        objectMapper = new ObjectMapper();
        //设置 date 格式
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * map to JSON
     * @param param
     * @return
     */
    public static String mapToJson(Map param) throws JsonProcessingException {
        return objectMapper.writeValueAsString(param);
    }


    /**
     * @param <T>
     * @return
     */
    public static <T> String objToJson(T t) throws JsonProcessingException {
        return objectMapper.writeValueAsString(t);
    }


    public static <T> T jsonToObj(String info,Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(info,clazz);
    }
}
