package lt.pavilonis.monpikas.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.Named;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public final class SystemUser extends Named<Long> implements UserDetails {

   @NotBlank
   private String username;

   @NotBlank
   private String password;

   @Email
   @NotBlank
   private String email;
   private boolean enabled;
   private List<Role> authorities = new ArrayList<>();

   public SystemUser(Long id, String name, String username, String password, String email, boolean enabled) {
      setId(id);
      setName(name);
      this.username = username;
      this.password = password;
      this.email = email;
      this.enabled = enabled;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return enabled;
   }
}
