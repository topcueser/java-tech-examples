package org.topcueser.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.topcueser.rabbitmq.ConnectionManager;
import org.topcueser.rabbitmq.LogNamesEnum;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class TestPublisher {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.exchangeDeclare("my-topic-exchange", BuiltinExchangeType.TOPIC, true);

        LogNamesEnum[] logLevels = LogNamesEnum.values();
        Random random = new Random();

        StringBuilder message = new StringBuilder();
        StringBuilder routeKey = new StringBuilder();
        for(int i = 0; i < 50; i++) {
            message.setLength(0);
            routeKey.setLength(0);

            LogNamesEnum randomLogLevel1 = logLevels[random.nextInt(logLevels.length)];
            LogNamesEnum randomLogLevel2 = logLevels[random.nextInt(logLevels.length)];
            LogNamesEnum randomLogLevel3 = logLevels[random.nextInt(logLevels.length)];

            routeKey.append(randomLogLevel1).append(".").append(randomLogLevel2).append(".").append(randomLogLevel3);
            message.append("Topic Exchange Log Type: ").append(randomLogLevel1).append(" - ").append(randomLogLevel2).append(" - ").append(randomLogLevel3);

            channel.basicPublish("my-topic-exchange", routeKey.toString(), null, message.toString().getBytes());
            System.out.println(message);
        }

        channel.close();
    }
}
