package lt.pavilonis.cmm.canteen.domain;

public class PupilRepresentation {

   public final String cardCode;
   public final String name;
   public final Meal meal;
   public final String grade;
   public final PupilType type;
   public final String base16photo;

   public PupilRepresentation(String cardCode, String name, Meal meal,
                              String grade, PupilType type, String base16photo) {

      this.cardCode = cardCode;
      this.name = name;
      this.meal = meal;
      this.grade = grade;
      this.type = type;
      this.base16photo = base16photo;
   }
}
