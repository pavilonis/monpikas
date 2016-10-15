package lt.pavilonis.monpikas.server.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public final class Pupil {

   public Pupil(String cardCode,
                String firstName,
                String lastName,
                String grade,
                LocalDate birthDate,
                PupilType pupilType,
                Set<Meal> meals,
                String comment,
                String photoPath) {

      this.cardCode = cardCode;
      this.firstName = firstName;
      this.lastName = lastName;
      this.grade = grade;
      this.birthDate = birthDate;
      this.meals = meals;
      this.pupilType = pupilType;
      this.comment = comment;
      this.photoPath = photoPath;
   }

   public final String cardCode;
   public final String firstName;
   public final String lastName;
   public final String grade;
   public final LocalDate birthDate;
   public final PupilType pupilType;
   public final Set<Meal> meals;
   public final String comment;
   public final String photoPath;

   public String name() {
      return this.firstName + " " + this.lastName;
   }
}
