package com.ljw.emmeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    METHODNOTFOUND(500,"未找到指定方法"),
    CLASSNOTFOUND(500,"未找到指定类");



    private final int code;
    private final String message;


}
