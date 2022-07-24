package com.ljw.emmeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ziyang
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 请求和响应状态码
     */
    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
