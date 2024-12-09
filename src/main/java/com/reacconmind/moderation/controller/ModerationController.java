package com.reacconmind.moderation.controller;

import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;
import com.reacconmind.moderation.service.ModerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moderation")
@Tag(name = "Content Moderation", description = "API endpoints for content moderation")
public class ModerationController {

    private static final Logger log = LoggerFactory.getLogger(ModerationController.class);

private final ModerationService moderationService;

public ModerationController(ModerationService moderationService) {
    this.moderationService = moderationService;
}

    @PostMapping("/moderate")
    @Operation(summary = "Moderate content", description = "Analyzes content for inappropriate or harmful material")
    public ResponseEntity<ContentModerationResponse> moderateContent(
            @Valid @RequestBody ContentModerationRequest request) {
        log.info("Received moderation request for content ID: {}", request.getContentId());
        ContentModerationResponse response = moderationService.moderateContent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{contentId}")
    @Operation(summary = "Get moderation history", description = "Retrieves moderation history for specific content")
    public ResponseEntity<List<ModerationResult>> getModerationHistory(
            @PathVariable String contentId) {
        log.info("Retrieving moderation history for content ID: {}", contentId);
        List<ModerationResult> history = moderationService.getContentModerationHistory(contentId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/category/{category}")
@Operation(summary = "Get moderations by category", description = "Retrieves moderations filtered by category")
public ResponseEntity<List<ModerationResult>> getModerationsByCategory(
        @PathVariable ModerationResult.ModerationCategory category) {
    log.info("Retrieving moderations for category: {}", category);
    List<ModerationResult> moderations = moderationService.getModerationsByCategory(category);
    return ResponseEntity.ok(moderations);
}

@Operation(summary = "Get moderations by content type", description = "Retrieves moderations filtered by content type and approval status")
public ResponseEntity<List<ModerationResult>> getModerationsByContentType(
        @PathVariable ModerationResult.ContentType contentType) {
    log.info("Retrieving moderations for content type: {}", contentType);
    List<ModerationResult> moderations = moderationService.getModerationsByContentType(contentType);
    return ResponseEntity.ok(moderations);
}
}
