package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.PupilType;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;

public class PupilDto {

   private long adbId;
   private long cardId;
   private String firstName;
   private String lastName;
   private PupilType pupilType;
   private String grade;
   private Set<Meal> meals = new HashSet<>();
   private Optional<Date> birthDate = empty();
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

   public Optional<Date> getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(Optional<Date> birthDate) {
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

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public long getAdbId() {
      return adbId;
   }

   public void setAdbId(long adbId) {
      this.adbId = adbId;
   }
}
