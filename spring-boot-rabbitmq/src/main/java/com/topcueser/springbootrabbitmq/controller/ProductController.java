package com.topcueser.springbootrabbitmq.controller;

import com.topcueser.springbootrabbitmq.dto.CreateProductDto;
import com.topcueser.springbootrabbitmq.dto.GetAttachmentDto;
import com.topcueser.springbootrabbitmq.dto.ResponseProductDto;
import com.topcueser.springbootrabbitmq.service.AttachmentService;
import com.topcueser.springbootrabbitmq.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.watermark.path}")
    private String uploadWatermarkPath;

    private final ProductService productService;
    private final AttachmentService attachmentService;

    public ProductController(ProductService productService, AttachmentService attachmentService) {
        this.productService = productService;
        this.attachmentService = attachmentService;
    }

    @PostMapping("/save")
    public ResponseProductDto saveProduct(@Valid @ModelAttribute CreateProductDto product,
                                           @Valid @RequestParam("file") MultipartFile uploadFile) throws Exception {
        return productService.saveProduct(product, uploadFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable Long id) throws Exception {
        ResponseProductDto responseProductDto = productService.getProductById(id);
        return  ResponseEntity.ok().body(responseProductDto);
    }

    @GetMapping("/{id}/show/image")
    public ResponseEntity<Resource> productShowImage(@PathVariable Long id) throws Exception {
        ResponseProductDto responseProductDto = productService.getProductById(id);
        Path imagePath = Paths.get(uploadPath + "/" + responseProductDto.attachment().fileName());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @GetMapping("/{id}/show/watermark/image")
    public ResponseEntity<Resource> productShowWatermarkImage(@PathVariable Long id) throws Exception {
        ResponseProductDto responseProductDto = productService.getProductById(id);
        Path imagePath = Paths.get(uploadWatermarkPath + "/" + responseProductDto.attachment().fileName());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String attachmentId) throws Exception {
        GetAttachmentDto getAttachmentDto = attachmentService.getAttachment(attachmentId);
        Path imagePath = Paths.get(uploadPath + "/" + getAttachmentDto.fileName());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + getAttachmentDto.fileName()
                                + "\"")
                .body(resource);
    }
}
