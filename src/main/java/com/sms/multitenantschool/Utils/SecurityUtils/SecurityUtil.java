package com.sms.multitenantschool.Utils.SecurityUtils;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtil {
    private SecurityUtil() {
    }
    public static Optional<String> getCurrentLoginTenant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return Optional.of(((UserDetails) principal).getUsername());
            } else if (principal instanceof String) {
                return Optional.of((String) principal);
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails)authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else {
            return authentication.getPrincipal() instanceof String ? (String)authentication.getPrincipal() : null;
        }
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).filter((authentication) -> authentication.getCredentials() instanceof String).map((authentication) -> (String)authentication.getCredentials());
    }

    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean var2;
        if (authentication != null) {
            Stream var10000 = getAuthorities(authentication);
            authority.getClass();
            if (var10000.anyMatch(authority::equals)) {
                var2 = true;
                return var2;
            }
        }

        var2 = false;
        return var2;
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

}
