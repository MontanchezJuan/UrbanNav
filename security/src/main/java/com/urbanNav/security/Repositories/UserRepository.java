package com.urbanNav.security.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.urbanNav.security.Models.User;

public interface UserRepository extends MongoRepository<User, String> {
    //Consultas Especializadas
    @Query("{'email': ?0}")
    public User getUserByEmail(String email);
}
