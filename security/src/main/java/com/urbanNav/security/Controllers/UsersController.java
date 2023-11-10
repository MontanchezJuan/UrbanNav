package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urbanNav.security.Models.CreditCard;
import com.urbanNav.security.Models.Role;
import com.urbanNav.security.Models.User;
import com.urbanNav.security.Models.UserProfile;
import com.urbanNav.security.Repositories.CreditCardRepository;
import com.urbanNav.security.Repositories.RoleRepository;
import com.urbanNav.security.Repositories.UserProfileRepository;
import com.urbanNav.security.Repositories.UserRepository;
import com.urbanNav.security.Services.EncryptionService;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RoleRepository theRoleRepository; // Se usa para el matchUserRole
    @Autowired
    private CreditCardRepository theCreditCardRepository;
    @Autowired
    private UserProfileRepository theUserProfileRepository;
    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.theUserRepository.findAll() != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body("listado de Usuarios" + "\n" + this.theUserRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay usuarios registrados");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar usuarios" + "\n" + e.toString());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            User theUser = this.theUserRepository
                    .findById(id)
                    .orElse(null);
            if (theUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body(theUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la busqueda del usuario" + "\n" + e.toString());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> udpate(@PathVariable String id, @RequestBody User theNewUser) {
        try {
            User theActualUser = this.theUserRepository.findById(id).orElse(null);
            if (theActualUser != null) {
                theActualUser.setName(theNewUser.getName());
                // se deberia poder actualizar al email??
                // en caso de que si, se debe verificar que el nuevo correo no lo tenga otro
                // usuario
                theActualUser.setEmail(theNewUser.getEmail());
                theActualUser.setPassword(encryptionService.convertirSHA256(theNewUser.getPassword()));
                theActualUser.setStatus(theNewUser.getStatus());
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado" + "\n" + theActualUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario a actualizar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la actualizacion del usuario" + "\n" + e.toString());
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            User theActualUser = this.theUserRepository.findById(id).orElse(null);
            if (theActualUser != null) {
                this.theUserRepository.delete(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se elimino correctamente al usuario:" + "\n" + theActualUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario a eliminar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la eliminacion del usuario" + "\n" + e.toString());
        }

    }

    @PutMapping("user/{user_id}/role/{role_id}")
    public ResponseEntity<?> matchUserRole(@PathVariable String user_id,
            @PathVariable String role_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            Role theActualRole = this.theRoleRepository.findById(role_id)
                    .orElse(null);
            if (theActualUser != null && theActualRole != null) {
                theActualUser.setRole(theActualRole);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se añadio correctamente el rol al usuario:" + "\n" + theActualUser);
            } else {
                if (theActualUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el rol");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el rol al usuario" + "\n" + e.toString());
        }
    }

    @PutMapping("user/{user_id}/role")
    public ResponseEntity<?> unMatchUserRole(@PathVariable String user_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            if (theActualUser != null) {
                theActualUser.setRole(null);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se removio correctamente el rol al usuario:" + "\n" + theActualUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al remover el rol al usuario" + "\n" + e.toString());
        }
    }

    @PutMapping("user/{user_id}/credit_card/{credit_card_id}")
    public ResponseEntity<?> matchUserCreditCard(@PathVariable String user_id,
            @PathVariable String credit_card_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            CreditCard theCreditCard = this.theCreditCardRepository.findById(credit_card_id)
                    .orElse(null);
            if (theActualUser != null && theCreditCard != null) {
                for (CreditCard creditCard : theActualUser.getCreditCards()) {
                    if (theCreditCard.equals(creditCard)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("El usuario ya contiene esta tarjeta de credito");
                    }
                }
                theActualUser.addCreditCard(theCreditCard);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se añadio correctamente la tarjeta de credito al usuario:" + "\n"
                                + theActualUser);
            } else {
                if (theActualUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro la tarjeta de credito");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir la tarjeta de credito al usuario" + "\n" + e.toString());
        }
    }

    @DeleteMapping("user/{user_id}/credit_card/{credit_card_id}")
    public ResponseEntity<?> unMatchUserCreditCard(@PathVariable String user_id, @PathVariable String credit_card_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            CreditCard theCreditCard = this.theCreditCardRepository.findById(credit_card_id).orElse(null);
            if (theActualUser != null && theCreditCard != null) {
                theActualUser.removeCreditCard(theCreditCard);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se removio correctamente la tarjeta de credito al usuario:" + "\n" + theActualUser);
            } else {
                if (theActualUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro la tarjeta de credito");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al remover la tarjeta de credito al usuario" + "\n" + e.toString());
        }
    }

    @PutMapping("user/{user_id}/user_profile/{user_profile_id}")
    public ResponseEntity<?> matchUserProfile(@PathVariable String user_id,
            @PathVariable String user_profile_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            UserProfile theUserProfile = this.theUserProfileRepository.findById(user_profile_id)
                    .orElse(null);
            if (theActualUser != null && theUserProfile != null) {
                theActualUser.setUserProfile(theUserProfile);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se añadio correctamente el perfil al usuario:" + "\n" + theActualUser);
            } else {
                if (theActualUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el perfil");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el perfil al usuario" + "\n" + e.toString());
        }
    }

    @PutMapping("user/{user_id}/user_profile")
    public ResponseEntity<?> unMatchUserProfile(@PathVariable String user_id) {
        try {
            User theActualUser = this.theUserRepository.findById(user_id)
                    .orElse(null);
            if (theActualUser != null) {
                theActualUser.setUserProfile(null);
                this.theUserRepository.save(theActualUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se removio correctamente el perfil al usuario:" + "\n" + theActualUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro al usuario");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al remover el perfil al usuario" + "\n" + e.toString());
        }
    }

}