package lt.pavilonis.cmm.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityCheckUtils {

   public static boolean hasRole(String role) {

      return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equalsIgnoreCase(role));
   }
}
