package lt.pavilonis.monpikas.security;

import lt.pavilonis.monpikas.common.Identified;
import lt.pavilonis.monpikas.common.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * Copied code from {@link org.springframework.security.core.authority.SimpleGrantedAuthority}
 * and implemented {@link Identified} interface
 */
public class Role extends Named<Long> implements GrantedAuthority {

   public Role(Long id, String name) {
      Assert.hasText(name, "A granted authority textual representation is required");
      setId(id);
      setName(name);
   }

   @Override
   public String getAuthority() {
      return getName();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj instanceof Role) {
         return getName().equals(((Role) obj).getName());
      }

      return false;
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public String toString() {
      return this.getName();
   }
}
