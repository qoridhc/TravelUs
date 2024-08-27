package com.ssafy.soltravel.util;

import com.ssafy.soltravel.exception.InvalidTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
        // 인스턴스화 방지
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new InvalidTokenException("No authentication found in context");
        }

        Long userId = (Long) authentication.getPrincipal();

        return userId;
    }
}
