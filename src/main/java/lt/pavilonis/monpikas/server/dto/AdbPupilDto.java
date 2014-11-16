package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Portion;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.empty;

public class AdbPupilDto {

   public AdbPupilDto() {
   }

   private long cardId;
   private String firstName;
   private String lastName;
   private Optional<LocalDate> birthDate = empty();
   private Optional<Portion> breakfastPortion = empty();
   private Optional<Portion> dinnerPortion = empty();
   private Optional<String> grade = empty();
   private Optional<String> comment = empty();

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

   public Optional<LocalDate> getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(Optional<LocalDate> birthDate) {
      this.birthDate = birthDate;
   }

   public Optional<String> getComment() {
      return comment;
   }

   public void setComment(Optional<String> comment) {
      this.comment = comment;
   }

   public Optional<Portion> getBreakfastPortion() {
      return breakfastPortion;
   }

   public void setBreakfastPortion(Optional<Portion> breakfastPortion) {
      this.breakfastPortion = breakfastPortion;
   }

   public Optional<Portion> getDinnerPortion() {
      return dinnerPortion;
   }

   public void setDinnerPortion(Optional<Portion> dinnerPortion) {
      this.dinnerPortion = dinnerPortion;
   }

   public Optional<String> getGrade() {
      return grade;
   }

   public void setGrade(Optional<String> grade) {
      this.grade = grade;
   }

   public long mealsPermitted() {
      long meals = 0;
      if (breakfastPortion.isPresent()) meals++;
      if (dinnerPortion.isPresent()) meals++;
      return meals;
   }
}
