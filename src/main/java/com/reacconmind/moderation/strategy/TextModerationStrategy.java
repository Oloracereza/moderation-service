package com.reacconmind.moderation.strategy;

import com.reacconmind.moderation.config.AzureConfig;
import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextModerationStrategy implements ModerationStrategy {

    private final RestTemplate restTemplate;
    private final AzureConfig azureConfig;

    @Override
    public ContentModerationResponse moderate(ContentModerationRequest request) {
        try {
            log.info("Moderating text content with ID: {}", request.getContentId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.set("Ocp-Apim-Subscription-Key", azureConfig.getKey());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> requestEntity = new HttpEntity<>(request.getContent(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                azureConfig.getTextModerationEndpoint(),
                requestEntity,
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                boolean containsOffensiveContent = responseBody.contains("\"Terms\":[") || 
                                                 responseBody.contains("\"Classification\":{\"ReviewRecommended\":true}");
                
                double confidenceScore = extractConfidenceScore(responseBody);
                ModerationResult.ModerationCategory category;
                String reason;

                if (containsOffensiveContent) {
                    if (confidenceScore > azureConfig.getAdultContentThreshold()) {
                        category = ModerationResult.ModerationCategory.ADULT;
                        reason = "Content contains adult material";
                    } else if (confidenceScore > azureConfig.getRacyContentThreshold()) {
                        category = ModerationResult.ModerationCategory.HARASSMENT;
                        reason = "Content contains harassment";
                    } else {
                        category = ModerationResult.ModerationCategory.HATE_SPEECH;
                        reason = "Content contains inappropriate language";
                    }
                } else {
                    category = ModerationResult.ModerationCategory.SAFE;
                    reason = "Content is safe";
                }

                return ContentModerationResponse.builder()
                    .contentId(request.getContentId())
                    .approved(!containsOffensiveContent)
                    .confidenceScore(confidenceScore)
                    .category(category)
                    .reason(reason)
                    .build();

            } else {
                throw new RuntimeException("Error from Azure Content Moderator API: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error moderating text content", e);
            return ContentModerationResponse.builder()
                .contentId(request.getContentId())
                .approved(false)
                .confidenceScore(1.0)
                .category(ModerationResult.ModerationCategory.OTHER)
                .reason("Error processing content: " + e.getMessage())
                .build();
        }
    }

    private double extractConfidenceScore(String responseBody) {
        try {
            if (responseBody.contains("\"Score\":")) {
                String score = responseBody.split("\"Score\":")[1].split(",")[0];
                return Double.parseDouble(score);
            }
            return 0.5; // Default confidence score
        } catch (Exception e) {
            return 0.5; // Default confidence score in case of parsing error
        }
    }

    @Override
    public boolean supports(ModerationResult.ContentType contentType) {
        return ModerationResult.ContentType.TEXT.equals(contentType);
    }
}
