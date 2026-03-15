package com.mind.trail.MindTrail;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import java.util.Optional;

public interface moodRepo extends MongoRepository<moodEntity, ObjectId> {
    Optional<moodEntity> findByUserId(ObjectId userId);
}
