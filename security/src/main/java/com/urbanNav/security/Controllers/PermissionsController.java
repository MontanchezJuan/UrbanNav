package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Repositories.PermissionRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public List<Permission> index() {
        return this.thePermissionRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission store(@RequestBody Permission newPermission) {
        return this.thePermissionRepository.save(newPermission);
    }

    @GetMapping("{id}")
    public Permission show(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
        return thePermission;
    }

    @PutMapping("{id}")
    public Permission udpate(@PathVariable String id, @RequestBody Permission theNewPermission) {
        Permission theActualPermission = this.thePermissionRepository.findById(id).orElse(null);
        if (theActualPermission != null) {
            theActualPermission.setRoute(theNewPermission.getRoute());
            theActualPermission.setMethod(theNewPermission.getMethod());
            theActualPermission.setDescription(theNewPermission.getDescription());
            return this.thePermissionRepository.save(theActualPermission);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }

    

}