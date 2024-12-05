package com.reacconmind.moderation.dto;

import jakarta.validation.constraints.NotNull;

public class BlockUserDTO {

    @NotNull(message = "User id must not be null")
    private Integer userId;

    @NotNull(message = "The user id to block must not be null")
    private Integer blockedUserId;  

    // Getters y Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBlockedUserId() { 
        return blockedUserId;
    }

    public void setBlockedUserId(Integer blockedUserId) { 
        this.blockedUserId = blockedUserId;
    }
}
