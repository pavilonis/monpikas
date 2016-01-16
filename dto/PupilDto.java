package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.PupilType;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public final class PupilDto {

   public PupilDto(long adbId, long cardId, String firstName, String lastName, String grade,
                   Optional<Date> birthDate, PupilType pupilType, Set<Meal> meals, Optional<String> comment) {

      this.adbId = adbId;
      this.cardId = cardId;
      this.firstName = firstName;
      this.lastName = lastName;
      this.grade = grade;
      this.birthDate = birthDate;
      this.meals = meals;
      this.pupilType = pupilType;
      this.comment = comment;
   }

   private final long adbId;
   private final long cardId;
   private final String firstName;
   private final String lastName;
   private final String grade;
   private final Optional<Date> birthDate;
   private final PupilType pupilType;
   private final Set<Meal> meals;
   private final Optional<String> comment;

   public long getAdbId() {
      return adbId;
   }

   public long getCardId() {
      return cardId;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public PupilType getPupilType() {
      return pupilType;
   }

   public String getGrade() {
      return grade;
   }

   public Set<Meal> getMeals() {
      return meals;
   }

   public Optional<Date> getBirthDate() {
      return birthDate;
   }

   public Optional<String> getComment() {
      return comment;
   }
}
