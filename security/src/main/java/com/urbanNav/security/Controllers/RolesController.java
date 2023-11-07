package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.Role;
import com.urbanNav.security.Repositories.RoleRepository;

import java.util.Arrays;


import com.urbanNav.security.Repositories.PermissionRepository;
import java.util.List;



@CrossOrigin
@RestController
@RequestMapping("roles")

public class RolesController {
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public List<Role> index() {
        return this.theRoleRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Role store(@RequestBody Role newRole) {

        return this.theRoleRepository.save(newRole);
    }

    @GetMapping("{id}")
    public Role show(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        return theRole;
    }

    @PutMapping("{id}")
    public Role udpate(@PathVariable String id, @RequestBody Role theNewRole) {
        Role theActualRole = this.theRoleRepository.findById(id).orElse(null);
        if (theActualRole != null) {
            theActualRole.setName(theNewRole.getName());
            theActualRole.setDescription(theNewRole.getDescription());
            return this.theRoleRepository.save(theActualRole);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Role theActualRole = this.theRoleRepository.findById(id).orElse(null);
        if (theActualRole != null) {
            this.theRoleRepository.delete(theActualRole);
        }
    }

    @PutMapping("role/{role_id}/permission/{permission_id}")
    public ResponseEntity<String> addPermissions(@PathVariable String role_id, @PathVariable String permission_id) {
        Role theActualRole = this.theRoleRepository.findById(role_id).orElse(null);
        Permission theActualPermission = this.thePermissionRepository.findById(permission_id).orElse(null);
    
        if (theActualRole != null && theActualPermission != null) {
            if (Arrays.stream(theActualRole.getTotalPermissions())
                .anyMatch(existingPermission -> existingPermission.get_id().equals(theActualPermission.get_id()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este permiso ya existe en este rol.");
            } else {
                theActualRole.addPermission(theActualPermission);
                this.theRoleRepository.save(theActualRole);
                return ResponseEntity.status(HttpStatus.OK).body("Permiso agregado con éxito.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol o permiso no encontrados.");
        }
    }
    


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("role/{role_id}/permission/{permission_id}")
    public void removeRolePermission(@PathVariable String role_id, @PathVariable String permission_id) {
        Role theActualRole = this.theRoleRepository.findById(role_id).orElse(null);
        Permission theActualPermission = thePermissionRepository.findById(permission_id).orElse(null);
        if (theActualRole != null) {
            theActualRole.removePermission(theActualPermission);
            this.theRoleRepository.save(theActualRole);
        }

    }
}