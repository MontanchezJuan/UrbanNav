package com.urbanNav.security.Controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanNav.security.Models.User;
import com.urbanNav.security.Models.UserProfile;
import com.urbanNav.security.Repositories.UserProfileRepository;

@CrossOrigin
@RestController
@RequestMapping("/profiles")
public class UserProfileController {
    @Autowired
    private UserProfileRepository theUserprofileRepository;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.theUserprofileRepository.findAll() != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body("listado de Perfiles" + "\n" + this.theUserprofileRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay perfiles registrados");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar perfiles" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<?> store(@RequestBody UserProfile userProfile) {
        try {
            UserProfile theActualProfile = this.theUserprofileRepository.getProfile(userProfile.getName(),
                    userProfile.getLastName(),
                    userProfile.getNumberPhone());
            if (theActualProfile != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un perfil con este nombre, apellido y telefono");
            } else {
                this.theUserprofileRepository.save(theActualProfile);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Perfil agregado con éxito." + "\n" + theActualProfile);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar crear el perfil" + "\n" + e.toString());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            UserProfile theUserProfile = this.theUserprofileRepository
                    .findById(id)
                    .orElse(null);
            if (theUserProfile != null) {
                return ResponseEntity.status(HttpStatus.OK).body(theUserProfile);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la busqueda del perfil" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserProfile theNewProfile) {
        try {
            UserProfile theProfile = this.theUserprofileRepository.findById(id).orElse(null);
            if (theProfile != null) {
                theProfile.setName(theNewProfile.getName());
                theProfile.setLastName(theNewProfile.getLastName());
                theProfile.setProfilePhoto(theNewProfile.getProfilePhoto());
                theProfile.setBirthday(theNewProfile.getBirthday());
                theProfile.setBackgroundImage(theNewProfile.getBackgroundImage());
                theProfile.setNumberPhone(theNewProfile.getNumberPhone());
                theProfile.setStatus(theNewProfile.getStatus());
                this.theUserprofileRepository.save(theProfile);
                return ResponseEntity.status(HttpStatus.OK).body("Perfil actualizado" + "\n" + theProfile);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al perfil a actualizar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la actualizacion del perfil" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            UserProfile theProfile = this.theUserprofileRepository.findById(id).orElse(null);
            if (theProfile != null) {
                this.theUserprofileRepository.delete(theProfile);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se elimino correctamente al perfil:" + "\n" + theProfile);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al perfil a eliminar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la eliminacion del perfil" + "\n" + e.toString());
        }
    }
}