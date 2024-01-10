package org.topcueser.rabbitmq.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.topcueser.rabbitmq.ConnectionManager;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchange {

    //Step-1: Declare the exchange
    public void declareExchange() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.exchangeDeclare("my-fanout-exchange", BuiltinExchangeType.FANOUT, true);
        channel.close();
    }

    //Step-2: Publish the messages
    public void publishMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < 50; i++) {
            message.setLength(0);
            message.append("Fanout message - ").append(i);
            channel.basicPublish("my-fanout-exchange", "", null, message.toString().getBytes());
            System.out.println(message);
        }
        channel.close();
    }

    //Step-3: Create the Subscribers
    public void subscribe1Message() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();

        //Create the Queue
        String queueName = "subscribe1-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        //Create bindings - (queue, exchange, routingKey)
        channel.queueBind(queueName, "my-fanout-exchange", "");

        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println(queueName + new String(message.getBody()));
        }), System.out::println);
    }

    public void subscribe2Message() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();

        //Create the Queue
        String queueName = "subscribe2-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        //Create bindings - (queue, exchange, routingKey)
        channel.queueBind(queueName, "my-fanout-exchange", "");

        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println(queueName + new String(message.getBody()));
        }), System.out::println);
    }

    public static void main(String[] args) {

        FanoutExchange fanoutExchangeExample = new FanoutExchange();

        try {
            fanoutExchangeExample.declareExchange();
        } catch (IOException | TimeoutException e ) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Thread publish = new Thread(() -> {
            try {
                fanoutExchangeExample.publishMessage();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        Thread subscribe = new Thread(() -> {
            try {
                fanoutExchangeExample.subscribe1Message();
                fanoutExchangeExample.subscribe2Message();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        subscribe.start();
        publish.start();
    }
}
