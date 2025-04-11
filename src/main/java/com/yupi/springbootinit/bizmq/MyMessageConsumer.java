package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.nio.channels.Channels;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/4/11
 *
 * @author Lantz
 * @version 1.0
 * @Description MyMessageConsumer
 * @since 1.8
 */

@Component
@Slf4j
public class MyMessageConsumer {

    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receiveMessage message = {}", message);
        channel.basicAck(deliveryTag, false);
    }

}
