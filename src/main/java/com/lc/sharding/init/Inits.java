package com.lc.sharding.init;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhangguoping
 * @version V1.0
 * @date 2017-04-09 16:51
 */
public class Inits {


    private static volatile ApplicationContext beanFactory = null;

    public void init() throws Exception {
        initSpring();
    }

    /**
     * 初始化Spring
     */
    public static void initSpring() {
        if (null == beanFactory) {
            synchronized (Inits.class) {
                if (null == beanFactory) {
                    try {
                        beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

}
