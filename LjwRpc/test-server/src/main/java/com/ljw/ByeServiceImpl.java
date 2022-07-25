package com.ljw;

import com.ljw.annotation.Service;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  11:48
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye"+name;
    }
}
