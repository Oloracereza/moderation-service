package com.reacconmind.moderation.strategy;

import com.reacconmind.moderation.config.AzureConfig;
import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageModerationStrategy implements ModerationStrategy {

    private final RestTemplate restTemplate;
    private final AzureConfig azureConfig;

    @Override
    public ContentModerationResponse moderate(ContentModerationRequest request) {
        try {
            log.info("Moderating image content with ID: {}", request.getContentId());

            // Decode base64 image
            byte[] imageBytes = Base64.getDecoder().decode(request.getContent());

            // Validate image size
            if (imageBytes.length > azureConfig.getMaxImageSizeBytes()) {
                return ContentModerationResponse.builder()
                    .contentId(request.getContentId())
                    .approved(false)
                    .confidenceScore(1.0)
                    .category(ModerationResult.ModerationCategory.OTHER)
                    .reason("Image size exceeds maximum allowed size")
                    .build();
            }

            // Prepare multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("Image", new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return "image.jpg";
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Ocp-Apim-Subscription-Key", azureConfig.getKey());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                azureConfig.getImageModerationEndpoint(),
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            // Process response
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                boolean isAdultContent = responseBody.contains("\"IsImageAdultClassified\":true");
                boolean isRacyContent = responseBody.contains("\"IsImageRacyClassified\":true");
                boolean containsOffensiveContent = isAdultContent || isRacyContent;

                ModerationResult.ModerationCategory category;
                String reason;
                double confidenceScore = extractConfidenceScore(responseBody);

                if (isAdultContent) {
                    category = ModerationResult.ModerationCategory.ADULT;
                    reason = "Image contains adult content";
                } else if (isRacyContent) {
                    category = ModerationResult.ModerationCategory.HARASSMENT;
                    reason = "Image contains racy content";
                } else {
                    category = ModerationResult.ModerationCategory.SAFE;
                    reason = "Image is safe";
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
            log.error("Error moderating image content", e);
            return ContentModerationResponse.builder()
                .contentId(request.getContentId())
                .approved(false)
                .confidenceScore(1.0)
                .category(ModerationResult.ModerationCategory.OTHER)
                .reason("Error processing image: " + e.getMessage())
                .build();
        }
    }

    private double extractConfidenceScore(String responseBody) {
        try {
            if (responseBody.contains("\"AdultClassificationScore\":")) {
                String score = responseBody.split("\"AdultClassificationScore\":")[1].split(",")[0];
                return Double.parseDouble(score);
            } else if (responseBody.contains("\"RacyClassificationScore\":")) {
                String score = responseBody.split("\"RacyClassificationScore\":")[1].split(",")[0];
                return Double.parseDouble(score);
            }
            return 0.5; // Default confidence score
        } catch (Exception e) {
            return 0.5; // Default confidence score in case of parsing error
        }
    }

    @Override
    public boolean supports(ModerationResult.ContentType contentType) {
        return ModerationResult.ContentType.IMAGE.equals(contentType);
    }
}
