package lt.pavilonis.monpikas.server.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private String name;

   @Column(unique = true)
   private String username;

   private String password;

   private Boolean enabled;

   @JoinTable(name = "User_Authority",
         joinColumns = {
               @JoinColumn(name = "user_id", referencedColumnName = "id")
         },
         inverseJoinColumns = {
               @JoinColumn(name = "authority_id", referencedColumnName = "id")
         }
   )
   @ManyToMany(fetch = FetchType.EAGER)
   private List<Authority> authorities = new ArrayList<>();

   public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setAuthorities(List<Authority> authorities) {
      this.authorities = authorities;
   }

   public void addAuthority(Authority auth) {
      this.authorities.add(auth);
   }

   //implementing 'UserDetails' interface methods
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
      for (Authority auth : authorities) {
         authList.add(new SimpleGrantedAuthority(auth.getRole()));
      }
      return authList;
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
