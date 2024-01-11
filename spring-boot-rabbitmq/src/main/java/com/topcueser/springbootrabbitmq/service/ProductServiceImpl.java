package com.topcueser.springbootrabbitmq.service;

import com.topcueser.springbootrabbitmq.dto.CreateProductDto;
import com.topcueser.springbootrabbitmq.dto.ResponseAttachmentDto;
import com.topcueser.springbootrabbitmq.dto.ResponseProductDto;
import com.topcueser.springbootrabbitmq.entity.Attachment;
import com.topcueser.springbootrabbitmq.entity.Product;
import com.topcueser.springbootrabbitmq.producer.AttachmentProducer;
import com.topcueser.springbootrabbitmq.repository.ProductRepository;
import com.topcueser.springbootrabbitmq.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${upload.path}")
    private String uploadPath;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final AttachmentProducer attachmentProducer;

    public ProductServiceImpl(ProductRepository productRepository, AttachmentProducer attachmentProducer) {
        this.productRepository = productRepository;
        this.attachmentProducer = attachmentProducer;
    }

    @Override
    public ResponseProductDto saveProduct(CreateProductDto createProductDto, MultipartFile uploadFile) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(uploadFile.getOriginalFilename()));
        try {

            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }

            String randomFileName = UUID.randomUUID() + "." + Helper.getExtensionByFilename(fileName);

            Attachment attachment = new Attachment(randomFileName, uploadFile.getContentType(), uploadFile.getSize());
            Product product = new Product(createProductDto.name(), createProductDto.price(), attachment);

            Product savedProduct = productRepository.save(product);

            Path fileNameAndPath = Paths.get(uploadPath, randomFileName).toAbsolutePath();
            Files.write(fileNameAndPath, uploadFile.getBytes());

            ResponseAttachmentDto responseAttachmentDto = new ResponseAttachmentDto(
                    randomFileName,
                    getDownloadUrl(savedProduct.getAttachment().getId()),
                    savedProduct.getAttachment().getFileType(),
                    savedProduct.getAttachment().getFileSize()
            );

            attachmentProducer.sendToQueue(savedProduct.getAttachment());

            return new ResponseProductDto(savedProduct.getName(), savedProduct.getPrice(), responseAttachmentDto);

        }catch (Exception e){
            logger.error("Could not save File: " + e.getMessage());
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public ResponseProductDto getProductById(Long id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new Exception("Product not found with Id: " + id));

        ResponseAttachmentDto responseAttachmentDto = new ResponseAttachmentDto(
                product.getAttachment().getFileName(),
                getDownloadUrl(product.getAttachment().getId()),
                product.getAttachment().getFileType(),
                product.getAttachment().getFileSize()
        );

        return new ResponseProductDto(product.getName(), product.getPrice(), responseAttachmentDto);
    }

    private String getDownloadUrl(String attachmentId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/api/products/download/")
                .path(attachmentId)
                .toUriString();
    }
}
