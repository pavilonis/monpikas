package lt.pavilonis.monpikas.server.domain;

import java.util.Set;

public final class PupilLocalData {
   private final String cardCode;
   private final PupilType type;
   private final String comment;
   private final Set<Meal> meals;

   public PupilLocalData(String cardCode, PupilType type, String comment, Set<Meal> meals) {
      this.cardCode = cardCode;
      this.type = type;
      this.comment = comment;
      this.meals = meals;
   }

   public String getCardCode() {
      return cardCode;
   }

   public PupilType getType() {
      return type;
   }

   public String getComment() {
      return comment;
   }

   public Set<Meal> getMeals() {
      return meals;
   }
}
