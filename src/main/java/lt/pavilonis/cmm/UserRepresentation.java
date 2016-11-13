package lt.pavilonis.cmm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class UserRepresentation {
   private String cardCode;
   private String firstName;
   private String lastName;
   private String description;
   private boolean pupil;
   private LocalDate birthDate;
   private String photoUrl;

   UserRepresentation() {/**/}

   UserRepresentation(@JsonProperty("cardCode") String cardCode,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("description") String description,
                      @JsonProperty("pupil") boolean pupil,
                      @JsonProperty("photoUrl") String photoUrl,
                      @JsonProperty("birthDate")
                      @JsonSerialize(using = LocalDateSerializer.class) LocalDate birthDate) {

      this.cardCode = cardCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.description = description;
      this.pupil = pupil;
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

   public boolean isPupil() {
      return pupil;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public String getPhotoUrl() {
      return photoUrl;
   }

   public void setCardCode(String cardCode) {
      this.cardCode = cardCode;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setPupil(boolean pupil) {
      this.pupil = pupil;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public void setPhotoUrl(String photoUrl) {
      this.photoUrl = photoUrl;
   }
}
