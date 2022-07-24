package com.ljw;

import com.ljw.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ziyang
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {


    @Override
    public String Hello(HelloObject helloObject) {
        log.info("接收到消息"+helloObject.getMessage());
        return "这是服务端的hello方法";
    }
}
