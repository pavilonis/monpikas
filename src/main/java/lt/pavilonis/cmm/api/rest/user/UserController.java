package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.school.user.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static lt.pavilonis.cmm.common.util.TimeUtils.duration;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;

@RequestMapping("/rest/users")
@RestController
public class UserController {

   private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getSimpleName());
   private final UserRepository userRepository;

   public UserController(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @GetMapping
   public List<User> loadAll(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String role,
                             @RequestParam(required = false) String group,
                             @RequestParam(required = false) Integer offset,
                             @RequestParam(required = false) Integer limit) {

      LocalDateTime opStart = LocalDateTime.now();
      UserFilter filter = new UserFilter(name, role, group, false)
            .withOffset(offset)
            .withLimit(limit);
      List<User> result = userRepository.load(filter);

      LOGGER.info("GET [number={}, filterName={}, filterRole={}, filterGroup={}, t={}]",
            result.size(), stripToEmpty(name), stripToEmpty(role), stripToEmpty(group), duration(opStart));
      return result;
   }

   @GetMapping("/{cardCode}")
   public ResponseEntity<User> load(@PathVariable String cardCode) {
      User result = userRepository.load(cardCode, true);
      return result == null
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            : ResponseEntity.ok().body(result);
   }

   @PutMapping
   public ResponseEntity<User> update(@RequestBody User user) {

      if (StringUtils.isBlank(user.getCardCode())) {

         LOGGER.error("User update FAIL: cardCode is blank [user={}]", user);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

      } else if (!userRepository.exists(user.getCardCode())) {

         LOGGER.error("User update FAIL: no matching user found [user={}]", user);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

      } else {

         User result = userRepository.update(user);
         LOGGER.info("User updated [user={}]", result);
         return ResponseEntity.ok().body(result);
      }
   }
}
