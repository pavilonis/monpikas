package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.common.Identifiable;
import lt.pavilonis.cmm.user.domain.UserRepresentation;

public final class UserMeal implements Identifiable<String> {
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

   @Override
   public String getId() {
      return this.mealData.getCardCode();
   }
}
