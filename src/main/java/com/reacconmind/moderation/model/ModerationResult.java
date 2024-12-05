package com.reacconmind.moderation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationResult {
    
    public enum ContentType {
        TEXT,
        IMAGE,
        VIDEO,
        AUDIO
    }

    public enum ModerationCategory {
        ADULT,          // Adult content
        HARASSMENT,     // Harassment or bullying
        HATE_SPEECH,    // Hate speech
        SAFE,          // Safe content
        SPAM,          // Spam content
        OFFENSIVE_CONTENT,      // Offensive content
        COPYRIGHT_VIOLATION,    // Copyright violation
        MISINFORMATION,        // Misinformation
        INAPPROPRIATE_LANGUAGE, // Inappropriate language
        OTHER           // Other violations
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String contentId;
    
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    
    private boolean approved;
    
    private double confidenceScore;
    
    @Enumerated(EnumType.STRING)
    private ModerationCategory category;
    
    private String reason;
}
