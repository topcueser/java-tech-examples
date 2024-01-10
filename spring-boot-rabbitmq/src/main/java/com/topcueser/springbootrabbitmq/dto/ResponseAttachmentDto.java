package com.topcueser.springbootrabbitmq.dto;

public record ResponseAttachmentDto(
        String fileName,
        String downloadUrl,
        String fileType,
        long fileSize
) {
}
