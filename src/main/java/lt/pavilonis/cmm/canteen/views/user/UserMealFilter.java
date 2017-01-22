package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.canteen.domain.MealType;

public final class UserMealFilter {
   private final MealType mealType;
   private final String text;

   public UserMealFilter(MealType mealType, String text) {
      this.mealType = mealType;
      this.text = text;
   }

   public MealType getMealType() {
      return mealType;
   }

   public String getText() {
      return text;
   }
}
