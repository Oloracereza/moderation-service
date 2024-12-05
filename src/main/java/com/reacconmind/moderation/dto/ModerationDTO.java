package com.reacconmind.moderation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModerationDTO {
    @Min(value = 1, message = "The publication ID must be a positive number.")
    private int publicationId;

    @Min(value = 1, message = "The user ID must be a positive number.")
    private int userId;

    @NotBlank(message = "The content cannot be empty.")
    @Size(max = 500, message = "The content cannot exceed 500 characters.")
    private String content;

    public ModerationDTO() {
        // Default constructor
    }

    // Constructor
    public ModerationDTO(int publicationId, int userId, String content) {
        this.publicationId = publicationId;
        this.userId = userId;
        this.content = content;
    }

    // Getters
    public int getPublicationId() {
        return publicationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
