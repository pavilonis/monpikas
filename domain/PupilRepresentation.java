package lt.pavilonis.monpikas.server.domain;

public class PupilRepresentation {

   public final String cardCode;
   public final String name;
   public final Meal meal;
   public final String grade;
   public final PupilType type;

   public PupilRepresentation(String cardCode, String name, Meal meal, String grade, PupilType type) {
      this.cardCode = cardCode;
      this.name = name;
      this.meal = meal;
      this.grade = grade;
      this.type = type;
   }
}
