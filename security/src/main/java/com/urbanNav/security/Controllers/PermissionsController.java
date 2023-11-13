package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Repositories.PermissionRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.thePermissionRepository.findAll() != null
                    && this.thePermissionRepository.findAll().isEmpty() == false) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.thePermissionRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay permisos registrados");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar permisos" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<?> store(@RequestBody Permission newPermission) {
        try {
            Permission thePermission = this.thePermissionRepository
                    .getPermission(newPermission.getRoute(), newPermission.getMethod())
                    .orElse(null);
            if (thePermission != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe este permiso");
            } else {
                this.thePermissionRepository.save(newPermission);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Permiso agregado con éxito." + "\n" + newPermission);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar crear el permiso" + "\n" + e.toString());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
            if (thePermission != null) {
                return ResponseEntity.status(HttpStatus.OK).body(thePermission);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el permiso");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la busqueda del permiso" + "\n" + e.toString());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> udpate(@PathVariable String id, @RequestBody Permission theNewPermission) {
        try {
            Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
            if (thePermission != null) {
                if ((thePermission.getRoute().equals(theNewPermission.getRoute()) == false
                        || thePermission.getMethod().equals(theNewPermission.getMethod()) == false)
                        && this.thePermissionRepository
                                .getPermission(theNewPermission.getRoute(), theNewPermission.getMethod())
                                .orElse(null) == null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Ya existe un permiso con esta ruta y metodo");
                } else {
                    thePermission.setRoute(theNewPermission.getRoute());
                    thePermission.setMethod(theNewPermission.getMethod());
                    thePermission.setDescription(theNewPermission.getDescription());
                    this.thePermissionRepository.save(thePermission);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Permiso actualizado" + "\n" + thePermission);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro el permiso a actualizar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la actualizacion del permiso" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
            if (thePermission != null) {
                this.thePermissionRepository.delete(thePermission);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se elimino correctamente el permiso:" + "\n" + thePermission);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro el permiso a eliminar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la eliminacion del permiso" + "\n" + e.toString());
        }
    }

}