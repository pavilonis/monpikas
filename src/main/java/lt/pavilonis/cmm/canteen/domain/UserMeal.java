package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.domain.UserRepresentation;

public final class UserMeal {
   private final UserRepresentation user;
   private final MealData mealData;

   public UserMeal(UserRepresentation user, MealData mealData) {
      this.user = user;
      this.mealData = mealData;
   }

   public UserRepresentation getUser() {
      return user;
   }

   public MealData getMealData() {
      return mealData;
   }
}
