package com.urbanNav.security.Controllers;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.User;
import com.urbanNav.security.Repositories.UserRepository;
import com.urbanNav.security.Services.EncryptionService;
import com.urbanNav.security.Services.JwtService;
import com.urbanNav.security.Services.ObjectMapperService;
import com.urbanNav.security.Services.ValidatorsService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ValidatorsService validatorService;

    @Autowired
    private ObjectMapperService objectMapperService;

    // Método login
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User theUser) throws IOException {
        try {
            String token = "";
            User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail()).orElse(null);
            if (actualUser != null
                    && actualUser.getPassword().equals(encryptionService.convertirSHA256(theUser.getPassword()))) {
                // Generar token
                token = jwtService.generateToken(actualUser);
                String finalJson = "{\"token\":\"" + token + "\"}";
                return ResponseEntity.status(HttpStatus.OK).body(finalJson);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\":\" Inicio de sesion incorrecto\" }");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\" Error al intentar iniciar sesion \",\"Error\":\"" + e.toString() + "\"}");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody User newUser) {
        try {
            User theActualUser = this.theUserRepository.getUserByEmail(newUser.getEmail()).orElse(null);
            if (theActualUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("{\"message\":\" Ya existe un usuario con este correo\" }");
            } else {
                newUser.setPassword(encryptionService.convertirSHA256(newUser.getPassword()));
                newUser.setCreated_at(LocalDateTime.now());
                this.theUserRepository.save(newUser);
                objectMapperService.writeToJSON(newUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("{\"message\":\" Usuario creado con exito\",\"user\":" + newUser + "}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\" Error al intentar crear el usuario\",\"error\":\"" + e.toString() + "\"}");
        }
    }

    // Método logout
    // Método reset pass
    @GetMapping("get-user")
    public ResponseEntity<?> getUser(final HttpServletRequest request) {
        try {
            User theUser = this.validatorService.getUser(request);
            if (theUser != null) {
                // Map<String, Object> userObject = new HashMap<>();
                // userObject.put("email", theUser.getEmail());
                // userObject.put("role", theUser.getRole());
                String userJSON = objectMapperService.writeToJSON(theUser);
                String finalJSON = "{\"user\":" + userJSON + "}";
                return ResponseEntity.status(HttpStatus.OK).body(finalJSON);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\" Token no valido\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\" Error al validar el token\",\"error\":\"" + e.toString() + "\"}");
        }
    }

    @PostMapping("permissions-validation")
    public ResponseEntity<?> permissionsValidation(final HttpServletRequest request,
            @RequestBody Permission thePermission) {
        try {
            boolean success = this.validatorService.validationRolePermission(request, thePermission.getRoute(),
                    thePermission.getMethod());
            if (success == true) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"permiso valido\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"permiso no valido\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"Error al validar el permiso\",\"" + e.toString() + "\"}");
        }

    }
}
