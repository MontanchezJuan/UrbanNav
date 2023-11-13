package com.urbanNav.security.Controllers;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.User;
import com.urbanNav.security.Repositories.UserRepository;
import com.urbanNav.security.Services.EncryptionService;
import com.urbanNav.security.Services.JwtService;
import com.urbanNav.security.Services.ValidatorsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

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

    // Método login
    @PostMapping("login")
    public String login(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
        String token = "";
        User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail()).orElse(null);
        if (actualUser != null
                && actualUser.getPassword().equals(encryptionService.convertirSHA256(theUser.getPassword()))) {
            // Generar token
            token = jwtService.generateToken(actualUser);
        } else {
            // manejar el problema
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return token;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody User newUser) {
        try {
            User theActualUser = this.theUserRepository.getUserByEmail(newUser.getEmail()).orElse(null);
            if (theActualUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con este correo");
            } else {
                newUser.setPassword(encryptionService.convertirSHA256(newUser.getPassword()));
                newUser.setCreated_at(LocalDateTime.now());
                this.theUserRepository.save(newUser);
                return ResponseEntity.status(HttpStatus.OK).body("Usuario agregado con éxito." + "\n" + newUser);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar crear el usuario" + "\n" + e.toString());
        }

    }

    // Método logout
    // Método reset pass
    @GetMapping("token-validation")
    public User tokenValidation(final HttpServletRequest request) {
        User theUser = this.validatorService.getUser(request);
        return theUser;
    }

    @PostMapping("permissions-validation")
    public boolean permissionsValidation(final HttpServletRequest request, @RequestBody Permission thePermission) {
        System.out.println(thePermission.toString());
        boolean success = this.validatorService.validationRolePermission(request, thePermission.getRoute(),
                thePermission.getMethod());
        return success;
    }

}
