package com.tangxianglin.blog.utils;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private int code = 200;
    private long total;
    private T data;

    public static <T> Result ok(String msg,T data){
        Result result = new Result();
        result.setMsg(msg);
        result.setData(data);
        System.out.println(result);
        return result;
    }

    public static <T> Result ok(String msg,T data,long total){
        Result result = new Result();
        result.setMsg(msg);
        result.setData(data);
        result.setTotal(total);
        return result;
    }

    public static Result ok(String msg){
        Result result = new Result();
        result.setMsg(msg);
        return result;
    }

    public static Result failure(String msg,int code){
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    public static Result failure(String msg){
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(500);
        return result;
    }

}
