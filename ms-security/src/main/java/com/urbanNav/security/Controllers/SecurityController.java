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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
        User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
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
