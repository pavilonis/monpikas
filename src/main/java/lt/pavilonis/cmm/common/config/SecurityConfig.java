package lt.pavilonis.cmm.common.config;

import lt.pavilonis.cmm.canteen.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(springSecurityUserDetailsService());
      authenticationProvider.setPasswordEncoder(passwordEncoder());

      auth.authenticationProvider(authenticationProvider);
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
            .authorizeRequests()
            .anyRequest().authenticated()
//            .anyRequest().permitAll()
//            .antMatchers("/**").authenticated()
            .antMatchers("/rest/**").hasRole("SCANNER")
//            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")

            .and()
            .httpBasic()
            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//            .and()

            .csrf().disable(); // csrf useful for browsers, not our case
//            .and();
//            .loginPage("/login")
//            .permitAll();
   }


   @Override
   public void configure(WebSecurity web) throws Exception {
//      web.ignoring().antMatchers("/");
   }

   @Bean
   public SecurityUserDetailsService springSecurityUserDetailsService() {
      return new SecurityUserDetailsService();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}