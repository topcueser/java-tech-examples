package com.topcueser.springbootrabbitmq.repository;

import com.topcueser.springbootrabbitmq.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
