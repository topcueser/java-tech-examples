package org.topcueser.rabbitmq.topic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.topcueser.rabbitmq.ConnectionManager;

import java.io.IOException;

public class TestConsumer {

    public static void main(String[] args) throws IOException {

        Channel channel = ConnectionManager.getConnection().createChannel();

        //Create the Queue
        String queueName = channel.queueDeclare().getQueue();
        String routeKey = "INFO.#";

        //Create bindings - (queue, exchange, routingKey)
        channel.queueBind(queueName, "my-topic-exchange", routeKey);

        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println(queueName + new String(message.getBody()));
        }), System.out::println);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println(new String(message.getBody(), "UTF-8"));
        };

        CancelCallback cancelCallback = System.out::println;
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
