package com.flamexander.rabbitmq.console.consumer;

import com.rabbitmq.client.*;
import java.util.Scanner;

public class BlogReceiver {
    private final static String EXCHANGER_NAME = "topic_php";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.TOPIC);
        String queue = channel.queueDeclare(EXCHANGER_NAME, true, false, false, null).getQueue();

        Scanner scanner = new Scanner(System.in);
        System.out.println(" [*] Введите: set_topic php");
        String topic = "";
        while (true) {
            String str = scanner.nextLine();
            String[] spString = str.split(" ");
            if(str.startsWith("/set_topic ")) {
                topic = spString[1];
                channel.queueBind(queue, EXCHANGER_NAME, topic);
            }
            if(str.startsWith("/other_topic ")){
                topic = spString[1];
                channel.queueBind(queue, EXCHANGER_NAME, topic);
            }
            System.out.println(" [*] Waiting for messages");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
            });
        }
    }
}
