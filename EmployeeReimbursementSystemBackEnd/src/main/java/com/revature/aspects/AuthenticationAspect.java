package com.revature.aspects;

import com.revature.annotations.RequiresRole;
import com.revature.models.DTOs.IncomingUserDTO;
import com.revature.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AuthenticationAspect {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationAspect(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Before("@annotation(requiresRole) && args(session,..)")
    public void checkRole(JoinPoint joinPoint, RequiresRole requiresRole, HttpSession session) {
        String requiredRole = requiresRole.value();
        Integer userId = (Integer) session.getAttribute("userId");
        if (!requiredRole.equals(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession().getAttribute("role"))) {
            throw new SecurityException("Access denied. Must be " + requiredRole);
        }
    }

    @Before("within(com.revature.controllers.*) " +
            "&& !execution(* com.revature.controllers.AuthenticationController.*(..))" +
            "&& !execution(* com.revature.controllers.DataPopulatorController.*(..))")
    public void checkLoggedIn() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);

        if (session != null) {
            System.out.println("Session ID: " + session.getId());
            System.out.println("Session UserId: " + session.getAttribute("userId"));

        } else {
            System.out.println("Session is null");
        }

        if (session == null || session.getAttribute("userId") == null) {
            throw new SecurityException("Access denied. Must be logged in");
        }
    }


}
