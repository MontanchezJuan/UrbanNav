package com.urbanNav.security.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urbanNav.security.Models.UserProfile;

/**
 * UserProfileRepository
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    
}