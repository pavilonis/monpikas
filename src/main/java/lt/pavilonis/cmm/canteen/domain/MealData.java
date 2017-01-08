package lt.pavilonis.cmm.canteen.domain;

import java.util.HashSet;
import java.util.Set;

public final class MealData {
   private String cardCode;
   private PupilType type;
   private String comment;
   private Set<Meal> meals = new HashSet<>();

   public MealData(String cardCode, PupilType type, String comment, Set<Meal> meals) {
      this.cardCode = cardCode;
      this.type = type;
      this.comment = comment;
      this.meals = meals;
   }

   public MealData(String cardCode) {
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
