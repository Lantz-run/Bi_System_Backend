package com.yupi.springbootinit.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/4/11
 *
 * @author Lantz
 * @version 1.0
 * @Description MyMessageProducerTest
 * @since 1.8
 */
@SpringBootTest
class MyMessageProducerTest {

    @Resource
    private MyMessageProducer myMessageProducer;

    @Test
    void sendMessage() {
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", "多读书多吃饭多睡觉");
    }
}