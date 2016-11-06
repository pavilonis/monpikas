package lt.pavilonis.cmm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class UserRepresentation {
   private final String cardCode;
   private final String firstName;
   private final String lastName;
   private final String description;
   private final boolean isStudent;
   private final LocalDate birthDate;
   private final String photoUrl;

   private UserRepresentation(@JsonProperty("cardCode") String cardCode,
                              @JsonProperty("firstName") String firstName,
                              @JsonProperty("lastName") String lastName,
                              @JsonProperty("description") String description,
                              @JsonProperty("isStudent") boolean isStudent,
                              @JsonProperty("photoUrl") String photoUrl,
                              @JsonProperty("birthDate")
                              @JsonSerialize(using = LocalDateSerializer.class) LocalDate birthDate) {

      this.cardCode = cardCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.description = description;
      this.isStudent = isStudent;
      this.photoUrl = photoUrl;
      this.birthDate = birthDate;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getDescription() {
      return description;
   }

   public boolean isStudent() {
      return isStudent;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public String getPhotoUrl() {
      return photoUrl;
   }
}
