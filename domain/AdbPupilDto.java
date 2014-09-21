package lt.pavilonis.monpikas.server.domain;

import java.time.LocalDate;

public class AdbPupilDto {

   public AdbPupilDto() {
   }

   private long cardId;
   private String firstName;
   private String lastName;
   private LocalDate birthDate;
   private boolean dinner;
   private String comment;

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long cardId) {
      this.cardId = cardId;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public boolean isDinner() {
      return dinner;
   }

   public void setDinner(boolean dinner) {
      this.dinner = dinner;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
