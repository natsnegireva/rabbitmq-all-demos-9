package com.flamexander.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.sun.xml.internal.ws.commons.xmlutil.Converter.UTF_8;

public class ItBlogApp {
    private final static String EXCHANGER_NAME = "topic_php";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.TOPIC);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String topicmessage = bufferedReader.readLine();
                if(topicmessage.contains(" ")) {
                    String[] topic_message = topicmessage.split(" ");
                    String topic = topic_message[0];
                    if(topic_message[1].isEmpty()) {
                        System.out.println("Error");
                    } else {
                        String messege = topicmessage.substring(topic.length() + 1);
                        String routingKey = "ping " + topic;
                        channel.basicPublish(EXCHANGER_NAME, routingKey, null, messege.getBytes(UTF_8));
                        System.out.println(" [x] Sent '" + routingKey + "';'" + messege + " '");
                    }
                } else {
                    System.out.println("Error");
                }
            }
        }

    }
}
