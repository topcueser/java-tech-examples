package com.topcueser.springbootrabbitmq.dto;

public record GetAttachmentDto(
        String fileId,
        String fileName,
        String fileType
) {
}
