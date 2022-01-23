package lt.pavilonis.monpikas.security;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.Named;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class LoginEvent extends Named<Long> {

   private LocalDateTime created;
   private String address;
   private boolean success;
   private boolean logout;

}
