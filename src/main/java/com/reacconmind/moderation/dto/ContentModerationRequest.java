package com.reacconmind.moderation.dto;

import com.reacconmind.moderation.model.ModerationResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentModerationRequest {
    
    @NotBlank(message = "Content ID cannot be blank")
    private String contentId;
    
    @NotBlank(message = "Content cannot be blank")
    private String content;
    
    @NotNull(message = "Content type cannot be null")
    private ModerationResult.ContentType contentType;
}
