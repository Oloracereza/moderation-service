package com.reacconmind.moderation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportPublicationDTO {

    @NotNull(message = "The publication ID cannot be null.")
    private Integer publicationId;

    @NotNull(message = "The moderator ID cannot be null.")
    private Integer moderatorId;

    @NotNull(message = "The reason for the report cannot be null.")
    @Size(min = 5, max = 500, message = "The reason must be between 5 and 500 characters.")
    private String reason;

    // Getters
    public Integer getPublicationId() {
        return publicationId;
    }

    public Integer getModeratorId() {
        return moderatorId;
    }

    public String getReason() {
        return reason;
    }

    // Setters
    public void setPublicationId(Integer publicationId) {
        this.publicationId = publicationId;
    }

    public void setModeratorId(Integer moderatorId) {
        this.moderatorId = moderatorId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
