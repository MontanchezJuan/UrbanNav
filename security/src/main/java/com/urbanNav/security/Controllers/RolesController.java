package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.Role;
import com.urbanNav.security.Repositories.RoleRepository;
import com.urbanNav.security.Repositories.PermissionRepository;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("api/role")

public class RolesController {
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public List<Role> index(){
        return this.theRoleRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Role store(@RequestBody Role newRole){
        return this.theRoleRepository.save(newRole);}

    @GetMapping("{id}")
    public Role show(@PathVariable String id){
        Role theRole=this.theRoleRepository
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
    public Role addPermissions(@PathVariable String id, @RequestBody String idPermission){
        Role theActualRole = this.theRoleRepository.findById(id).orElse(null); //Verifica que el role exista en la bd
        Permission theActualPermission = this.thePermissionRepository.findById(id).orElse(null); // verifica que el permission exista en la bd
        if (theActualRole != null && theActualPermission != null){
            theActualRole.addPermission(theActualPermission); // añade el array de permission al role
            return this.theRoleRepository.save(theActualRole); // guarda
        }else{
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("role/{role_id}/permission/{permission_id}")
    public void removeRolePermission(@PathVariable String id){
        Role theActualRole = this.theRoleRepository.findById(id).orElse(null);
        Permission theActualPermission = thePermissionRepository.findById(id).orElse(null);
        if (theActualRole != null){
            theActualRole.removePermission(theActualPermission);
            this.theRoleRepository.save(theActualRole);
        }

    }
}