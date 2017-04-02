package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.common.Identifiable;
import lt.pavilonis.cmm.ui.security.Role;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SecurityUser implements UserDetails, Identifiable<Long> {

   private Long id;
   private String name;
   private String username;
   private String password;
   private String email;
   private boolean enabled;
   private List<Role> authorities = new ArrayList<>();

   public SecurityUser() {
   }

   public SecurityUser(Long id, String name, String username, String password, String email, boolean enabled) {
      this.id = id;
      this.name = name;
      this.username = username;
      this.password = password;
      this.email = email;
      this.enabled = enabled;
   }

   @Override
   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
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

   public void setName(String name) {
      this.name = name;
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
