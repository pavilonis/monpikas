package lt.pavilonis.monpikas.key;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.pavilonis.monpikas.scanlog.Scanner;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.common.Identified;

import java.time.LocalDateTime;

public class Key extends Identified<Integer> {

   private final int keyNumber;
   private final LocalDateTime dateTime;
   private final User user;
   private final Scanner scanner;
   private final KeyAction keyAction;

   public Key(@JsonProperty("keyNumber") int keyNumber,
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

   @Override
   public Integer getId() {
      return getKeyNumber();
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
