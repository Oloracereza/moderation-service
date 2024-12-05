package com.reacconmind.moderation.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moderation {

    @EmbeddedId
    private ModerationPK id;   

    @NotBlank(message = "The reason should not be blank")
    private String reason;  

    @NotNull
    @FutureOrPresent(message = "The date must be today or in the future")
    private LocalDateTime moderationDate;  

    private Integer userId;

    private Integer reportedUserId;

    private Integer publicationId;

    @Enumerated(EnumType.STRING)
    private ModerationType moderationType;
}
