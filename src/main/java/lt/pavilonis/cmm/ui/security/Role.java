package lt.pavilonis.cmm.ui.security;

import lt.pavilonis.cmm.common.Identifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * Copied code from {@link org.springframework.security.core.authority.SimpleGrantedAuthority}
 * and implemented {@link Identifiable} interface
 */
public class Role implements GrantedAuthority, Identifiable<Long> {

   private final String role;
   private final Long id;

   public Role(Long id, String role) {
      Assert.hasText(role, "A granted authority textual representation is required");
      this.id = id;
      this.role = role;
   }

   @Override
   public Long getId() {
      return id;
   }

   @Override
   public String getAuthority() {
      return role;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj instanceof Role) {
         return role.equals(((Role) obj).role);
      }

      return false;
   }

   public int hashCode() {
      return this.role.hashCode();
   }

   public String toString() {
      return this.role;
   }
}
