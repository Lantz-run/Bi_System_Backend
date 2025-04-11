package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/4/11
 *
 * @author Lantz
 * @version 1.0
 * @Description MqInitMain
 * @since 1.8
 */

/**
 * 用于创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
 */
public class MqInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // 创建队列
            String queueName1 = "code_queue";
            // 声明队列，设置队列为持久化的，非独占的，非自动删除的
            channel.queueDeclare(queueName1, true, false, false, null);
            // 将队列绑定到指定的交换机上
            channel.queueBind(queueName1, EXCHANGE_NAME, "my_routingKey");
        } catch (Exception e){

        }
    }
}
