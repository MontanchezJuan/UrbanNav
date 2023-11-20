package com.urbanNav.security.Repositories;

import com.urbanNav.security.Models.Permission;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    @Query("{'route':?0,'method':?1}")
    Optional<Permission> getPermission(String route, String method);
}
