package com.reacconmind.moderation.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ModerationPK implements Serializable {

    private String contentId;
    private String userId;

    public ModerationPK() {}

    public ModerationPK(String contentId, String userId) {
        this.contentId = contentId;
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModerationPK that = (ModerationPK) o;
        return Objects.equals(contentId, that.contentId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId, userId);
    }
}
