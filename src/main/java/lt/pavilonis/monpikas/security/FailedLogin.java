package lt.pavilonis.monpikas.security;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.Named;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class FailedLogin extends Named<Long> {

   LocalDateTime created;
   String address;

}
