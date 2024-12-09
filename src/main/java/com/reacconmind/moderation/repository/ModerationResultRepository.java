package com.reacconmind.moderation.repository;

import com.reacconmind.moderation.model.ModerationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModerationResultRepository extends JpaRepository<ModerationResult, Long> {
    List<ModerationResult> findByContentId(String contentId);
    List<ModerationResult> findByCategory(ModerationResult.ModerationCategory category);
    List<ModerationResult> findByContentTypeAndApproved(ModerationResult.ContentType contentType, boolean approved);
    List<ModerationResult> findByContentType(ModerationResult.ContentType contentType);
}
