package com.reacconmind.moderation.service;

import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;
import com.reacconmind.moderation.repository.ModerationResultRepository;
import com.reacconmind.moderation.strategy.ModerationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationService {

    private static final Logger log = LoggerFactory.getLogger(ModerationService.class);

    private final List<ModerationStrategy> moderationStrategies;
    private final ModerationResultRepository moderationResultRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Transactional
    public ContentModerationResponse moderateContent(ContentModerationRequest request) {
        log.info("Processing moderation request for content type: {}", request.getContentType());

        ModerationStrategy strategy = moderationStrategies.stream()
                .filter(s -> s.supports(request.getContentType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No moderation strategy found for content type: " + request.getContentType()));

        ContentModerationResponse response = strategy.moderate(request);

        // Save moderation result
        ModerationResult result = ModerationResult.builder()
                .contentId(request.getContentId())
                .contentType(ModerationResult.ContentType.valueOf(request.getContentType().name()))
                .approved(response.isApproved())
                .confidenceScore(response.getConfidenceScore())
                .category(ModerationResult.ModerationCategory.valueOf(response.getCategory().name()))
                .reason(response.getReason())
                .build();

        moderationResultRepository.save(result);
        log.info("Moderation result saved for content ID: {}", request.getContentId());

        return response;
    }

    public List<ModerationResult> getContentModerationHistory(String contentId) {
        return moderationResultRepository.findByContentId(contentId);
    }

    public List<ModerationResult> getModerationsByCategory(ModerationResult.ModerationCategory category) {
        return moderationResultRepository.findByCategory(category);
    }

    public List<ModerationResult> getModerationsByContentType(ModerationResult.ContentType contentType) {
        return moderationResultRepository.findByContentType(contentType);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
