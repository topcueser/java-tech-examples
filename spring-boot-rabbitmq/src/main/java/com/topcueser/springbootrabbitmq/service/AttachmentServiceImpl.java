package com.topcueser.springbootrabbitmq.service;

import com.topcueser.springbootrabbitmq.dto.GetAttachmentDto;
import com.topcueser.springbootrabbitmq.dto.ResponseAttachmentDto;
import com.topcueser.springbootrabbitmq.entity.Attachment;
import com.topcueser.springbootrabbitmq.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public GetAttachmentDto getAttachment(String attachmentId) throws Exception {
        Attachment attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + attachmentId));
        return new GetAttachmentDto(attachmentId, attachment.getFileName(), attachment.getFileType());
    }
}
