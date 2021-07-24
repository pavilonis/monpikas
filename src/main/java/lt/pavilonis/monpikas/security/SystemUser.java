package lt.pavilonis.monpikas.security;

import lt.pavilonis.monpikas.common.Named;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

   public SystemUser() {/**/}

   public SystemUser(Long id, String name, String username, String password, String email, boolean enabled) {
      setId(id);
      setName(name);
      this.username = username;
      this.password = password;
      this.email = email;
      this.enabled = enabled;
   }

   @Override
   public String getPassword() {
      return password;
   }

   public String getEmail() {
      return email;
   }

   public boolean getEnabled() {
      return enabled;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public Collection<Role> getAuthorities() {
      return this.authorities;
   }

   public void setAuthorities(List<Role> authorities) {
      this.authorities = authorities;
   }

   @Override
   public String getUsername() {
      return username;
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
