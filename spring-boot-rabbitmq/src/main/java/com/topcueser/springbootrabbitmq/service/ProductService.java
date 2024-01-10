package com.topcueser.springbootrabbitmq.service;

import com.topcueser.springbootrabbitmq.dto.CreateProductDto;
import com.topcueser.springbootrabbitmq.dto.ResponseProductDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponseProductDto saveProduct(CreateProductDto createProductDto, MultipartFile uploadFile) throws Exception;
    ResponseProductDto getProductById(Long id) throws Exception;
}
