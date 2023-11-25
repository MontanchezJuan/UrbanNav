package com.urbanNav.security.Controllers;

import java.util.List;

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
import com.urbanNav.security.Services.JSONResponsesService;

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
    @Autowired
    private JSONResponsesService jsonResponsesService;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.theUserRepository.findAll() != null) {
                List<User> users = this.theUserRepository.findAll();
                this.jsonResponsesService.setData(users);
                this.jsonResponsesService.setMessage("Lista de Usuarios encontrada correctamente");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No hay usuarios registrados");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al buscar usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            User theUser = this.theUserRepository
                    .findById(id)
                    .orElse(null);
            if (theUser != null) {
                this.jsonResponsesService.setData(theUser);
                this.jsonResponsesService.setMessage("usuario encontrado con exito");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro al usuario");
                ;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la busqueda del usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> udpate(@PathVariable String id, @RequestBody User theNewUser) {
        try {
            User theActualUser = this.theUserRepository.findById(id).orElse(null);
            if (theActualUser != null) {
                if (theActualUser.getEmail().equals(theNewUser.getEmail()) == false
                        && this.theUserRepository.getUserByEmail(theNewUser.getEmail()).orElse(null) != null) {
                    this.jsonResponsesService.setMessage("Este correo ya le pertenece a otro usuario");
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(this.jsonResponsesService.getFinalJSON());

                } else {
                    theActualUser.setEmail(theNewUser.getEmail());
                    theActualUser.setPassword(encryptionService.convertirSHA256(theNewUser.getPassword()));
                    theActualUser.setStatus(theNewUser.getStatus());
                    this.theUserRepository.save(theActualUser);
                    this.jsonResponsesService.setData(theActualUser);
                    this.jsonResponsesService.setMessage("Usuario actualizado");
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(this.jsonResponsesService.getFinalJSON());
                }
            } else {
                this.jsonResponsesService.setMessage("No se encontro al usuario a actualizar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());

            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la actualizacion del usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            User theActualUser = this.theUserRepository.findById(id).orElse(null);
            if (theActualUser != null) {
                this.theUserRepository.delete(theActualUser);
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se elimino correctamente al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro al usuario a eliminar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la eliminacion del usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se añadio correctamente el rol al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                if (theActualUser == null) {
                    this.jsonResponsesService.setMessage("No se encontro al usuario");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                } else {
                    this.jsonResponsesService.setMessage("No se encontro el rol");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                }
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al añadir el rol al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se removio correctamente el rol al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro al usuario");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al remover el rol al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                        this.jsonResponsesService.setMessage("El usuario ya contiene esta tarjeta de credito");
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(this.jsonResponsesService.getFinalJSON());
                    }
                }
                theActualUser.addCreditCard(theCreditCard);
                this.theUserRepository.save(theActualUser);
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se añadio correctamente la tarjeta de credito al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                if (theActualUser == null) {
                    this.jsonResponsesService.setMessage("No se encontro al usuario");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                } else {
                    this.jsonResponsesService.setMessage("No se encontro la tarjeta de credito");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                }
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al añadir la tarjeta de credito al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se removio correctamente la tarjeta de credito al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                if (theActualUser == null) {
                    this.jsonResponsesService.setMessage("No se encontro al usuario");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                } else {
                    this.jsonResponsesService.setMessage("No se encontro la tarjeta de credito");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                }
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al remover la tarjeta de credito al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se añadio correctamente el perfil al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                if (theActualUser == null) {
                    this.jsonResponsesService.setMessage("No se encontro al usuario");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                } else {
                    this.jsonResponsesService.setMessage("No se encontro el perfil");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
                }
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al añadir el perfil al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                this.jsonResponsesService.setData(theActualUser);
                this.jsonResponsesService.setMessage("Se removio correctamente el perfil al usuario");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro al usuario");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al remover el perfil al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

}