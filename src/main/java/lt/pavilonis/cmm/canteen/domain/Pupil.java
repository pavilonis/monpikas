package lt.pavilonis.monpikas.server.domain;

import java.time.LocalDate;
import java.util.Set;

public final class Pupil {

   public Pupil(String cardCode,
                String firstName,
                String lastName,
                String grade,
                LocalDate birthDate,
                PupilType type,
                Set<Meal> meals,
                String comment,
                String photoPath) {

      this.cardCode = cardCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.grade = grade;
      this.birthDate = birthDate;
      this.meals = meals;
      this.type = type;
      this.comment = comment;
      this.photoPath = photoPath;
   }

   private final String cardCode;
   private final String firstName;
   private final String lastName;
   private final String grade;
   private final LocalDate birthDate;
   private final PupilType type;
   private final Set<Meal> meals;
   private final String comment;
   private final String photoPath;

   public String name() {
      return this.firstName + " " + this.lastName;
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

   public String getGrade() {
      return grade;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public PupilType getType() {
      return type;
   }

   public Set<Meal> getMeals() {
      return meals;
   }

   public String getComment() {
      return comment;
   }

   public String getPhotoPath() {
      return photoPath;
   }
}
