package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.security.LoginEvent.LoginEventBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class AuthenticationEventListener {

   private final LoginEventRepository repository;

   @EventListener
   public void authenticationSuccess(AuthenticationSuccessEvent event) {
      saveEvent(event.getAuthentication(), true, false);
      log.info("Login successful");
   }

   @EventListener
   public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
      saveEvent(event.getAuthentication(), false, false);
      log.warn("Login failed");
   }

   @EventListener
   public void userLoggedOut(LogoutSuccessEvent event) {
      saveEvent(event.getAuthentication(), true, true);
      log.warn("User logged out");
   }

   private void saveEvent(Authentication auth, boolean success, boolean logout) {
      Object details = auth.getDetails();

      LoginEventBuilder<?, ?> builder = LoginEvent.builder()
            .success(success)
            .logout(logout)
            .name(auth.getName());

      if (details instanceof WebAuthenticationDetails) {
         builder.address(((WebAuthenticationDetails) details).getRemoteAddress());
      }

      repository.saveOrUpdate(builder.build());
   }

}
