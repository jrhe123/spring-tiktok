package com.imooc.exceptions;

import com.imooc.grace.result.ResponseStatusEnum;

public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}