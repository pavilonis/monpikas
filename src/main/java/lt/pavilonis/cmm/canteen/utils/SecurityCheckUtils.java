package lt.pavilonis.monpikas.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityCheckUtils {

   public static boolean hasRole(Authentication authentication, String role) {
      for (GrantedAuthority auth : authentication.getAuthorities()) {
         if (auth.getAuthority().equalsIgnoreCase(role)) {
            return true;
         }
      }
      return false;
   }
}
