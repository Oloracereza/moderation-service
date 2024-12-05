package com.reacconmind.moderation.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ModerationPK implements Serializable {

    private Integer userId;
    private Integer reportedUserId;
    private Integer publicationId;
    private Integer moderationTypeId;

    public ModerationPK() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public Integer getPublicationId() {
        return publicationId;
    }

    public void setPublication(Integer publicationId) {
        this.publicationId = publicationId;
    }

    public Integer getModerationTypeId() {
        return moderationTypeId;
    }

    public void setModerationTypeId(Integer moderationTypeId) {
        this.moderationTypeId = moderationTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModerationPK that = (ModerationPK) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(reportedUserId, that.reportedUserId) &&
               Objects.equals(publicationId, that.publicationId) &&
               Objects.equals(moderationTypeId, that.moderationTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, reportedUserId, publicationId, moderationTypeId);
    }
}
