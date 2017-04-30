package lt.pavilonis.cmm.key.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.pavilonis.cmm.common.Identifiable;
import lt.pavilonis.cmm.user.domain.User;

import java.time.LocalDateTime;

public class Key implements Identifiable<String> {
   private final int keyNumber;
   private final LocalDateTime dateTime;
   private final User user;
   private final Scanner scanner;
   private final KeyAction keyAction;

   public Key(@JsonProperty("keyNumber") int keyNumber,
//                            @JsonSerialize(using = IsoLocalDateTimeSerializer.class)
              @JsonProperty("dateTime") LocalDateTime dateTime,
              @JsonProperty("user") User user,
              @JsonProperty("scanner") Scanner scanner,
              @JsonProperty("keyAction") KeyAction keyAction) {

      this.keyNumber = keyNumber;
      this.dateTime = dateTime;
      this.user = user;
      this.scanner = scanner;
      this.keyAction = keyAction;
   }

   public int getKeyNumber() {
      return keyNumber;
   }

   public LocalDateTime getDateTime() {
      return dateTime;
   }

   public User getUser() {
      return user;
   }

   public Scanner getScanner() {
      return scanner;
   }

   public KeyAction getKeyAction() {
      return keyAction;
   }
}
