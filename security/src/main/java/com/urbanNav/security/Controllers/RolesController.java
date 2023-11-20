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

@CrossOrigin
@RestController
@RequestMapping("roles")

public class RolesController {
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.theRoleRepository.findAll() != null && this.theRoleRepository.findAll().isEmpty() == false) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.theRoleRepository.findAll());
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
    public ResponseEntity<?> store(@RequestBody Role newRole) {
        try {
            Role theActualRole = this.theRoleRepository.getRole(newRole.getName()).orElse(null);
            if (theActualRole != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un rol con este nombre");
            } else {
                this.theRoleRepository.save(newRole);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Rol agregado con éxito." + "\n" + newRole);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar crear el rol" + "\n" + e.toString());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            Role theRole = this.theRoleRepository
                    .findById(id)
                    .orElse(null);
            if (theRole != null) {
                return ResponseEntity.status(HttpStatus.OK).body(theRole);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al rol");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la busqueda del rol" + "\n" + e.toString());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> udpate(@PathVariable String id, @RequestBody Role theNewRole) {
        try {
            Role theRole = this.theRoleRepository.findById(id).orElse(null);
            if (theRole != null) {
                if (theRole.getName().equals(theNewRole.getName()) == false
                        && this.theRoleRepository.getRole(theNewRole.getName()).orElse(null) != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un rol con este nombre");
                } else {
                    theRole.setName(theNewRole.getName());
                    theRole.setDescription(theNewRole.getDescription());
                    theRole.setStatus(theNewRole.getStatus());
                    this.theRoleRepository.save(theRole);
                    return ResponseEntity.status(HttpStatus.OK).body("Rol actualizado" + "\n" + theRole);

                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al rol a actualizar");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la actualizacion del rol" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            Role theRole = this.theRoleRepository.findById(id).orElse(null);
            if (theRole != null) {
                this.theRoleRepository.delete(theRole);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se elimino correctamente al perfil:" + "\n" + theRole);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al perfil a eliminar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la eliminacion del perfil" + "\n" + e.toString());
        }
    }

    @PutMapping("role/{role_id}/permission/{permission_id}")
    public ResponseEntity<String> addPermissions(@PathVariable String role_id, @PathVariable String permission_id) {
        try {
            Role theActualRole = this.theRoleRepository.findById(role_id).orElse(null);
            Permission theActualPermission = this.thePermissionRepository.findById(permission_id).orElse(null);
            if (theActualRole != null && theActualPermission != null) {
                if (theActualRole.getTotalPermissions() != null && Arrays.stream(theActualRole.getTotalPermissions())
                        .anyMatch(existingPermission -> existingPermission.get_id()
                                .equals(theActualPermission.get_id()))) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Este permiso ya existe en este rol.");
                } else {
                    theActualRole.addPermission(theActualPermission);
                    this.theRoleRepository.save(theActualRole);
                    return ResponseEntity.status(HttpStatus.OK).body("Permiso agregado con éxito." + "\n"
                            + theActualRole);
                }
            } else {
                if (theActualRole == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permiso no encontrado.");
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el permiso al rol" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("role/{role_id}/permission/{permission_id}")
    public ResponseEntity<?> removeRolePermission(@PathVariable String role_id, @PathVariable String permission_id) {
        try {
            Role theActualRole = this.theRoleRepository.findById(role_id).orElse(null);
            Permission theActualPermission = thePermissionRepository.findById(permission_id).orElse(null);
            if (theActualRole != null && theActualPermission != null) {
                theActualRole.removePermission(theActualPermission);
                this.theRoleRepository.save(theActualRole);
                return ResponseEntity.status(HttpStatus.OK).body("Permiso removido con éxito." + "\n"
                        + theActualRole);
            } else {
                if (theActualRole == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permiso no encontrado.");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al remover el permiso al rol" + "\n" + e.toString());
        }

    }
}