package com.urbanNav.security.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.urbanNav.security.Models.UserProfile;

/**
 * UserProfileRepository
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    @Query("{'name':?0,'lastname':?1,'numberPhone':?2}")
    UserProfile getProfile(String name, String lastname, String numberPhone);
}