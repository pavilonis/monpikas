package lt.pavilonis.monpikas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private final UserDetailsService userDetailsService;
   private final PasswordEncoder passwordEncoder;

   public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
      this.userDetailsService = userDetailsService;
      this.passwordEncoder = passwordEncoder;
   }

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) {
      var authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(userDetailsService);
      authenticationProvider.setPasswordEncoder(passwordEncoder);
      auth.authenticationProvider(authenticationProvider);
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
            .httpBasic()
            .and()
            .authorizeRequests()
//            .mvcMatchers("/rest/**").hasRole("SCANNER")
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
   }
}