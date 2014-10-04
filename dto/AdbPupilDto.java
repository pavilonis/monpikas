package lt.pavilonis.monpikas.server.dto;

import java.time.LocalDate;

public class AdbPupilDto {

   public AdbPupilDto() {
   }

   private long cardId;
   private String firstName;
   private String lastName;
   private LocalDate birthDate;
   private boolean dinnerPermitted;
   private boolean breakfastPermitted;
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

   public boolean isDinnerPermitted() {
      return dinnerPermitted;
   }

   public void setDinnerPermitted(boolean dinnerPermitted) {
      this.dinnerPermitted = dinnerPermitted;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public boolean isBreakfastPermitted() {
      return breakfastPermitted;
   }

   public void setBreakfastPermitted(boolean breakfastPermitted) {
      this.breakfastPermitted = breakfastPermitted;
   }

   public long mealsPermitted() {
      long meals = 0;
      if (isBreakfastPermitted()) meals++;
      if (isDinnerPermitted()) meals++;
      return meals;
   }
}
