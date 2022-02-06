package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Component
public class AuthenticationEventListener {

   private final LoginEventRepository repository;
   private final HttpServletRequest request;

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

      LoginEvent loginEvent = LoginEvent.builder()
            .success(success)
            .logout(logout)
            .name(auth.getName())
            .address(getAddress())
            .build();

      repository.saveOrUpdate(loginEvent);
   }

   private String getAddress() {
      String xfHeader = request.getHeader("X-Forwarded-For");
      if (xfHeader == null) {
         return request.getRemoteAddr();
      }
      return xfHeader.split(",")[0];
   }

}
