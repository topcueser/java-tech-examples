package com.topcueser.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.topcueser.springbootrabbitmq.entity.Attachment;
import com.topcueser.springbootrabbitmq.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class AttachmentConsumer {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.watermark.path}")
    private String uploadWatermarkPath;
    private static final Logger logger = LoggerFactory.getLogger(AttachmentConsumer.class);

    @RabbitListener(queues = "${demo.rabbit.queue.name}")
    public void handleMessage(Attachment attachment,
                              Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) {

        try {

            logger.info(String.format("Consuming message: %s", attachment.getFileName()));

            String watermarkText = "esertopcu";
            String fileType = Helper.getExtensionByFilename(attachment.getFileName());

            File source = new File(uploadPath + File.separator + attachment.getFileName());
            File destination = new File(uploadWatermarkPath + File.separator + attachment.getFileName());

            addTextWatermark(watermarkText, fileType, source, destination);

            channel.basicAck(tag, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void addTextWatermark(String text, String type, File source, File destination) throws IOException {
        BufferedImage image = ImageIO.read(source);

        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);
        w.setColor(Color.GRAY);
        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 75));
        FontMetrics fontMetrics = w.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, w);

        // calculate center of the image
        int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
        int centerY = image.getHeight() / 2;

        // add text overlay to the image
        w.drawString(text, centerX, centerY);
        ImageIO.write(watermarked, type, destination);
        w.dispose();
    }

}
