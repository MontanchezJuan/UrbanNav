package com.urbanNav.security.Repositories;

import com.urbanNav.security.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    @Query("{'url':?0,'method':?1")
    Permission getPermission(String url, String method);
}

