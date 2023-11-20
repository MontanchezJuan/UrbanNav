package com.urbanNav.security.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.urbanNav.security.Models.UserProfile;

/**
 * UserProfileRepository
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    @Query("{'numberPhone':?0}")
    Optional<UserProfile> getProfile(String numberPhone);
}