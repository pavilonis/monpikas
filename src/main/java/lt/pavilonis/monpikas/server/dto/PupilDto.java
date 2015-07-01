package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.PupilType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;

public class PupilDto {

   public PupilDto() {
   }

   private long adbId;
   private long cardId;
   private String firstName;
   private String lastName;
   private PupilType pupilType;
   private Set<Meal> meals = new HashSet<>();
   private Optional<LocalDate> birthDate = empty();
   //   private Optional<Portion> breakfastPortion = empty();
//   private Optional<Portion> dinnerPortion = empty();
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

   public Set<Meal> getMeals() {
      return meals;
   }

   public void setMeals(Set<Meal> meals) {
      this.meals = meals;
   }

   public PupilType getPupilType() {
      return pupilType;
   }

   public void setPupilType(PupilType pupilType) {
      this.pupilType = pupilType;
   }

   //   public Optional<Portion> getBreakfastPortion() {
//      return breakfastPortion;
//   }
//
//   public void setBreakfastPortion(Optional<Portion> breakfastPortion) {
//      this.breakfastPortion = breakfastPortion;
//   }
//
//   public Optional<Portion> getDinnerPortion() {
//      return dinnerPortion;
//   }
//
//   public void setDinnerPortion(Optional<Portion> dinnerPortion) {
//      this.dinnerPortion = dinnerPortion;
//   }

   public Optional<String> getGrade() {
      return grade;
   }

   public void setGrade(Optional<String> grade) {
      this.grade = grade;
   }

   public long getAdbId() {
      return adbId;
   }

   public void setAdbId(long adbId) {
      this.adbId = adbId;
   }
}
