package com.urbanNav.security.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.urbanNav.security.Models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
}
