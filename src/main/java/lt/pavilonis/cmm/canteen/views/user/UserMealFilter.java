package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.canteen.domain.MealType;

public final class UserMealFilter {
   private final MealType mealType;
   private final String text;
   private final boolean withMealAssigned;

   public UserMealFilter(MealType mealType, String text, boolean withMealAssigned) {
      this.mealType = mealType;
      this.text = text;
      this.withMealAssigned = withMealAssigned;
   }

   public MealType getMealType() {
      return mealType;
   }

   public String getText() {
      return text;
   }

   public boolean isWithMealAssigned() {
      return withMealAssigned;
   }
}
