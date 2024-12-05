package com.reacconmind.moderation.controller;

import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;
import com.reacconmind.moderation.service.ModerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moderation")
@Tag(name = "Content Moderation", description = "API endpoints for content moderation")
public class ModerationController {

    private final ModerationService moderationService;

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

    @GetMapping("/type/{contentType}")
    @Operation(summary = "Get moderations by content type", description = "Retrieves moderations filtered by content type and approval status")
    public ResponseEntity<List<ModerationResult>> getModerationsForContentType(
            @PathVariable ModerationResult.ContentType contentType,
            @RequestParam(defaultValue = "true") boolean approved) {
        log.info("Retrieving {} moderations for content type: {}", approved ? "approved" : "rejected", contentType);
        List<ModerationResult> moderations = moderationService.getModerationsForContentType(contentType, approved);
        return ResponseEntity.ok(moderations);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifies if the moderation service is up and running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Moderation service is running");
    }
}
