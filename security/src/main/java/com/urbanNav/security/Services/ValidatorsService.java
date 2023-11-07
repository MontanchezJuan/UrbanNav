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
        boolean success = false;
        User theUser = this.getUser(request);
        if (theUser != null) {
            Role theRole = theUser.getRole();
            url = url.replaceAll("[0-9a-fA-F]{24}", "?");
            Permission thePermission = thePermissionRepository.getPermission(url,
                    method);

            if (theRole != null && thePermission != null) {
                for (Permission permission : theRole.getTotalPermissions()) {
                    if (permission.equals(thePermission)) {
                        success = true;
                    }
                }
            } else {
                success = false;
            }

        }
        if (success == false) {
            System.out.println("no tiene este permiso");
        }
        return success;
    }

    public String getUrlString(String url) {
        String newUrl = "";
        return newUrl;
    }

    public User getUser(final HttpServletRequest request) {
        User theUser = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            User theUserFromToken = jwtService.getUserFromToken(token);
            if (theUserFromToken != null) {
                theUser = this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);
                theUser.setPassword("");
            }
        }
        return theUser;
    }
}
