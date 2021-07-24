package lt.pavilonis.monpikas.scanlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lt.pavilonis.monpikas.key.Key;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.common.serialization.IsoLocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class ScanLog {

   private final LocalDateTime dateTime;
   private final User user;
   private final List<Key> keys;

   public ScanLog(@JsonSerialize(using = IsoLocalDateTimeSerializer.class)
                  @JsonProperty("dateTime") LocalDateTime dateTime,
                  @JsonProperty("user") User user,
                  @JsonProperty("keys") List<Key> keys) {

      this.dateTime = dateTime;
      this.user = user;
      this.keys = keys;
   }

   public LocalDateTime getDateTime() {
      return dateTime;
   }

   public User getUser() {
      return user;
   }

   public List<Key> getKeys() {
      return keys;
   }
}
