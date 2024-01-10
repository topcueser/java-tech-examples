package org.topcueser.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.topcueser.rabbitmq.ConnectionManager;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectExchange {

    //Step-1: Declare the exchange
    public void declareExchange() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.exchangeDeclare("my-direct-exchange", BuiltinExchangeType.DIRECT, true);
        channel.close();
    }

    //Step-2: Declare the Queues
    public void declareQueues() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();

        //Create the Queues
        channel.queueDeclare("info-queue", true, false, false, null);
        channel.queueDeclare("error-queue", true, false, false, null);
        channel.queueDeclare("warning-queue", true, false, false, null);

        channel.close();
    }

    //Step-3: Create the Bindings
    public void declareBindings() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey)
        channel.queueBind("info_queue", "my-direct-exchange", "info-route");
        channel.queueBind("error-queue", "my-direct-exchange", "error-route");
        channel.queueBind("warning-queue", "my-direct-exchange", "warning-route");
        channel.close();
    }

    //Step-4: Publish the messages
    public void publishMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < 50; i++) {
            message.setLength(0);
            message.append("Direct Exchange Info Message - ").append(i);
            channel.basicPublish("my-direct-exchange", "info-route", null, message.toString().getBytes());
            System.out.println(message);
        }

        for(int i = 0; i < 20; i++) {
            message.setLength(0);
            message.append("Direct Exchange Error Message - ").append(i);
            channel.basicPublish("my-direct-exchange", "error-route", null, message.toString().getBytes());
            System.out.println(message);
        }

        for(int i = 0; i < 10; i++) {
            message.setLength(0);
            message.append("Direct Exchange Warning Message - ").append(i);
            channel.basicPublish("my-direct-exchange", "warning-route", null, message.toString().getBytes());
            System.out.println(message);
        }

        channel.close();
    }

    //Step-5: Create the Subscribers
    public void subscribeMessage() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume("info_queue", true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println("info_queue:" + new String(message.getBody()));
        }), System.out::println);

        channel.basicConsume("error-queue", true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println("error-queue:" + new String(message.getBody()));
        }), System.out::println);

        channel.basicConsume("warning-queue", true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println("warning-queue:" + new String(message.getBody()));
        }), System.out::println);
    }

    public static void main(String[] args){

        DirectExchange directExchangeExample = new DirectExchange();

        try {
            directExchangeExample.declareExchange();
            directExchangeExample.declareQueues();
            directExchangeExample.declareBindings();
        } catch (IOException | TimeoutException e ) {
            throw new RuntimeException(e);
        }

        Thread publish = new Thread(() -> {
            try {
                directExchangeExample.publishMessage();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        //Threads created to publish-subscribe asynchronously
        Thread subscribe = new Thread(() -> {
            try {
                directExchangeExample.subscribeMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        subscribe.start();
        publish.start();
    }

}
