package com.example.domain.entity;

import lombok.Data;

@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;


    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(200, null, null);
    }

    public static Result success(Object data) {
        return new Result(200, null, data);
    }

    public static Result error(String message) {
        return new Result(-1, message, null);
    }

}
