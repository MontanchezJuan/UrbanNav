package com.urbanNav.security.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.urbanNav.security.Services.ValidatorsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private ValidatorsService validatorService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
            throws Exception {
        try {
            if (request.getHeader("Authorization") == null) {
                System.err.println("No se encuentra Authorization");
                return false;
            }
            boolean success = this.validatorService.validationRolePermission(request,
                    request.getRequestURI(),
                    request.getMethod());
            if (success == true) {
                System.out.println("Permiso valido");
                return success;
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Acceso denegado");
                return success;
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error al procesar la peticion");
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el
        // controlador
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la
        // renderización de la vista
    }
}
