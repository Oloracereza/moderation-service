package com.reacconmind.moderation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModerationResponseDTO {
    @Min(value = 1, message = "The publication ID must be a positive number.")
    private int publicationId;

    @NotBlank(message = "The status cannot be empty.")
    @Size(max = 20, message = "The status cannot exceed 20 characters.")
    private String status;

    @NotBlank(message = "The message cannot be empty.")
    @Size(max = 200, message = "The message cannot exceed 200 characters.")
    private String message;

    public ModerationResponseDTO(int publicationId, String status, String message) {
        this.publicationId = publicationId;
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
