package com.topcueser.springbootrabbitmq.service;

import com.topcueser.springbootrabbitmq.dto.GetAttachmentDto;
import com.topcueser.springbootrabbitmq.dto.ResponseAttachmentDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    GetAttachmentDto getAttachment(String attachmentId) throws Exception;
}
