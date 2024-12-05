package com.reacconmind.moderation.dto;

import com.reacconmind.moderation.model.ModerationResult;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentModerationResponse {
    private String contentId;
    private boolean approved;
    private double confidenceScore;
    private ModerationResult.ModerationCategory category;
    private String reason;
    
    public static ContentModerationResponse fromModerationResult(ModerationResult result) {
        return ContentModerationResponse.builder()
                .contentId(result.getContentId())
                .approved(result.isApproved())
                .confidenceScore(result.getConfidenceScore())
                .category(result.getCategory())
                .reason(result.getReason())
                .build();
    }
}
