package com.applyai.applyai.security;

import com.applyai.applyai.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        return getCurrentUser().userId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().email();
    }

    private static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new UnauthorizedException("Not authenticated");
        }
        return user;
    }
}