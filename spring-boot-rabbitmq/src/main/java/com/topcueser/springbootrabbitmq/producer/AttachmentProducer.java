package com.topcueser.springbootrabbitmq.producer;

import com.topcueser.springbootrabbitmq.entity.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AttachmentProducer {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentProducer.class);

    @Value("${demo.rabbit.routing.name}")
    private String routingKey;

    @Value("${demo.rabbit.exchange.name}")
    private String exchangeName;
    private final AmqpTemplate amqpTemplate;

    public AttachmentProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToQueue(Attachment attachment) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, attachment);
        logger.info(String.format("Sending message ID: %s", attachment.getFileName()));
    }
}
