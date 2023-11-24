package com.urbanNav.security.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.Role;
import com.urbanNav.security.Models.User;
import com.urbanNav.security.Repositories.PermissionRepository;
import com.urbanNav.security.Repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private UserRepository theUserRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    public boolean validationRolePermission(HttpServletRequest request, String url, String method) {
        if (url.equals("/roles/656021611ae5d15c7d6d2517") && method.equals("DELETE")) {
            return false;
        }
        User theUser = this.getUser(request);
        if (theUser != null) {
            Role theRole = theUser.getRole();
            url = url.replaceAll("[0-9a-fA-F]{24}", "?");
            url = url.replaceAll("\\d+", "?");
            if (theRole.get_id().equals("656021611ae5d15c7d6d2517")) {
                return true;
            }
            Permission thePermission = thePermissionRepository.getPermission(url,
                    method).orElse(null);
            System.out.println(theRole);

            if (theRole != null && thePermission != null) {
                for (Permission permission : theRole.getTotalPermissions()) {
                    if (permission.equals(thePermission)) {
                        return true;
                    }
                }
            } else {
                System.out.println("no tiene este permiso");
                return false;
            }

        }
        System.out.println("el usuario no existe");
        return false;

    }

    public User getUser(final HttpServletRequest request) {
        User theUser = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            Boolean isValid = jwtService.validateToken(token);
            if (isValid == true) {
                User theUserFromToken = jwtService.getUserFromToken(token);
                if (theUserFromToken != null) {
                    theUser = this.theUserRepository.findById(theUserFromToken.get_id())
                            .orElse(null);
                    theUser.setPassword("");
                }
            }
        }
        return theUser;
    }

}
