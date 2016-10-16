package lt.pavilonis.monpikas.server.domain;

import java.util.HashSet;
import java.util.Set;

public final class PupilLocalData {
   private String cardCode;
   private PupilType type;
   private String comment;
   private Set<Meal> meals = new HashSet<>();

//   public PupilLocalData() {/**/}

   public PupilLocalData(String cardCode, PupilType type, String comment, Set<Meal> meals) {
      this.cardCode = cardCode;
      this.type = type;
      this.comment = comment;
      this.meals = meals;
   }

   public PupilLocalData(String cardCode) {
      this.cardCode = cardCode;
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

   public void setCardCode(String cardCode) {
      this.cardCode = cardCode;
   }

   public void setType(PupilType type) {
      this.type = type;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public void setMeals(Set<Meal> meals) {
      this.meals = meals;
   }
}
