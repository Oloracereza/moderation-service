package com.reacconmind.moderation.repository;

import com.reacconmind.moderation.model.Moderation;
import com.reacconmind.moderation.model.ModerationPK;
import com.reacconmind.moderation.model.ModerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModerationRepository extends JpaRepository<Moderation, ModerationPK> {
    List<Moderation> findByPublicationId(Integer publicationId);
    List<Moderation> findByUserId(Integer userId);
    List<Moderation> findByModerationType(ModerationType moderationType);
}
