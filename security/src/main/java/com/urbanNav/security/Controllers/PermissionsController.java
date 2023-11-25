package com.urbanNav.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Repositories.PermissionRepository;
import com.urbanNav.security.Services.JSONResponsesService;

@CrossOrigin
@RestController
@RequestMapping("permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private JSONResponsesService jsonResponsesService;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.thePermissionRepository.findAll() != null
                    && this.thePermissionRepository.findAll().isEmpty() == false) {
                List<Permission> permissions = this.thePermissionRepository.findAll();
                this.jsonResponsesService.setData(permissions);
                this.jsonResponsesService.setMessage("Lista de permisos encontrada con exito");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No hay permisos registrados");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al buscar permisos");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setMessage("Ya existe este permiso");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.thePermissionRepository.save(newPermission);
                this.jsonResponsesService.setData(thePermission);
                this.jsonResponsesService.setMessage("Permiso agregado con éxito");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al intentar crear el permiso");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
            if (thePermission != null) {
                this.jsonResponsesService.setData(thePermission);
                this.jsonResponsesService.setMessage("Permiso encontrado con exito");
                return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro el permiso");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la busqueda del permiso");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                    this.jsonResponsesService.setMessage("Ya existe un permiso con esta ruta y metodo");
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(this.jsonResponsesService.getFinalJSON());
                } else {
                    thePermission.setRoute(theNewPermission.getRoute());
                    thePermission.setMethod(theNewPermission.getMethod());
                    thePermission.setDescription(theNewPermission.getDescription());
                    this.thePermissionRepository.save(thePermission);
                    this.jsonResponsesService.setData(thePermission);
                    this.jsonResponsesService.setMessage("Permiso actualizado");
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(this.jsonResponsesService.getFinalJSON());
                }
            } else {
                this.jsonResponsesService.setMessage("No se encontro el permiso a actualizar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la actualizacion del permiso");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
            if (thePermission != null) {
                this.thePermissionRepository.delete(thePermission);
                this.jsonResponsesService.setData(thePermission);
                this.jsonResponsesService.setMessage("Se elimino correctamente el permiso");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro el permiso a eliminar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la eliminacion del permiso");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

}