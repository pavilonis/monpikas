package lt.pavilonis.cmm.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityCheckUtils {

   public static boolean hasRole(Authentication authentication, String role) {

      return authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equalsIgnoreCase(role));
   }
}
