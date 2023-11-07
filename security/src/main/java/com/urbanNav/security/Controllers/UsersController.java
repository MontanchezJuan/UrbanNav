package com.urbanNav.security.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.urbanNav.security.Models.Role;
import com.urbanNav.security.Models.User;
import com.urbanNav.security.Repositories.RoleRepository;
import com.urbanNav.security.Repositories.UserRepository;
import com.urbanNav.security.Services.EncryptionService;
import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RoleRepository theRoleRepository; // Se usa para el matchUserRole
    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("")
    public List<User> index() {
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User show(@PathVariable String id) {
        User theUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        return theUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public User store(@RequestBody User newUser) {
        newUser.setPassword(encryptionService.convertirSHA256(newUser.getPassword()));
         // nueva contraseña del usuario
         newUser.setCreated_at(LocalDateTime.now());                                                                              // encriptada
        return this.theUserRepository.save(newUser);
    }

    @PutMapping("{id}")
    public User udpate(@PathVariable String id, @RequestBody User theNewUser) {
        User theActualUSer = this.theUserRepository.findById(id).orElse(null);
        if (theActualUSer != null) {
            theActualUSer.setName(theNewUser.getName());
            theActualUSer.setEmail(theNewUser.getEmail());
            theActualUSer.setPassword(encryptionService.convertirSHA256(theNewUser.getPassword()));
            theActualUSer.setStatus(theNewUser.getStatus());
            return this.theUserRepository.save(theActualUSer);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        User theActualUSer = this.theUserRepository.findById(id).orElse(null);
        if (theActualUSer != null) {
            this.theUserRepository.delete(theActualUSer);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("user/{user_id}/role/{role_id}")
    public User matchUserRole(@PathVariable String user_id,
            @PathVariable String role_id) {
        User theActualUSer = this.theUserRepository.findById(user_id)
                .orElse(null);
        Role theActualRole = this.theRoleRepository.findById(role_id)
                .orElse(null);
        if (theActualUSer != null && theActualRole != null) {
            theActualUSer.setRole(theActualRole);
            return this.theUserRepository.save(theActualUSer);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{user_id}/role")
    public User unMatchUserRole(@PathVariable String user_id) {
        User theActualUSer = this.theUserRepository.findById(user_id)
                .orElse(null);
        if (theActualUSer != null) {
            theActualUSer.setRole(null);
            return this.theUserRepository.save(theActualUSer);
        } else {
            return null;
        }
    }
}