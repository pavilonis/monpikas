package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class AuthenticationEventListener {

   private final FailedLoginRepository repository;

   @EventListener
   public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
      Authentication auth = event.getAuthentication();
      Object details = auth.getDetails();

      FailedLogin.FailedLoginBuilder<?, ?> builder = FailedLogin.builder()
            .name(auth.getName());

      if (details instanceof WebAuthenticationDetails) {
         builder.address(((WebAuthenticationDetails) details).getRemoteAddress());
      }

      repository.saveOrUpdate(builder.build());
      log.warn("Saved failed login attempt");
   }

}
