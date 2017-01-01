package lt.pavilonis.cmm.canteen.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public final class SpringSecurityUser implements UserDetails {

   private final String name;
   private final String username;
   private final String password;
   private final boolean enabled;
   private final Set<String> roles;

   public SpringSecurityUser(String name, String username, String password, boolean enabled, Set<String> roles) {
      this.name = name;
      this.username = username;
      this.password = password;
      this.enabled = enabled;
      this.roles = roles;
   }

   public String getName() {
      return name;
   }

   @Override
   public String getPassword() {
      return password;
   }

   public boolean getEnabled() {
      return enabled;
   }

   public Set<String> getRoles() {
      return roles;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
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
