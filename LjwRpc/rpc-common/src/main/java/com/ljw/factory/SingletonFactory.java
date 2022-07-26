package com.ljw.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-25  16:56
 * @Description: TODO
 * @Version: 1.0
 */
public class SingletonFactory {
    private static Map<Class,Object> objectMap=new HashMap<>();
    private SingletonFactory(){}
    public static <T> T getInstance(Class<T> clazz){
        Object instance = objectMap.get(clazz);
        synchronized (clazz){
            if (instance==null){
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz,instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}
