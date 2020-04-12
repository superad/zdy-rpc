package com.mengma.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fgm
 * @description 上线文工具类
 * @date 2020-04-12
 ***/
@Component
public class ApplicationContextConfig implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext getContext(){
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
