package lt.pavilonis.monpikas.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RequestMapping("/rest/users")
@RestController
public class UserRestController {

   private final UserRepository repository;

   @PostMapping
   public ResponseEntity<User> createUser(@RequestBody UserRepresentation userRepresentation) {

      User user = User.builder()
            .name(userRepresentation.getName())
            .cardCode(userRepresentation.getCardCode())
            .organizationGroup(userRepresentation.getOrganizationGroup())
            .organizationRole(userRepresentation.getOrganizationRole())
            .build();

      Assert.hasText(user.getCardCode(), "Not expecting user without card code");
      Assert.hasText(user.getName(), "Not expecting user without name");
      log.info("Creating user {}", user);

      User createdUser = repository.create(user);
      return ResponseEntity.ok().body(createdUser);
   }

   @Value
   public static class UserRepresentation {
      String name;
      String cardCode;
      String organizationRole;
      String organizationGroup;
   }
}
