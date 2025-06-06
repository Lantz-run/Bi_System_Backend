package com.yupi.springbootinit.model.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class FanoutConsumer {
  private static final String EXCHANGE_NAME = "fanout-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel1 = connection.createChannel();
    Channel channel2 = connection.createChannel();

    // 声明交换机
    channel1.exchangeDeclare(EXCHANGE_NAME, "fanout");
    // 创建队列，随机分配一个队列名称
    String queueName1 = "小王的任务队列";
    channel1.queueDeclare(queueName1, true, false, false, null);
    channel1.queueBind(queueName1, EXCHANGE_NAME, "");

    // 声明交换机
    channel2.exchangeDeclare(EXCHANGE_NAME, "fanout");
    // 创建队列，随机分配一个队列名称
    String queueName2 = "小李的任务队列";
    channel2.queueDeclare(queueName2, true, false, false, null);
    channel2.queueBind(queueName2, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [小王] Received '" + message + "'");
    };

    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [小李] Received '" + message + "'");
    };
    channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    channel2.basicConsume(queueName1, true, deliverCallback2, consumerTag -> { });
  }
}