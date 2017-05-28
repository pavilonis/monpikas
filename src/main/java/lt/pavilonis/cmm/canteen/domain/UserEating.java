package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.common.Identified;

public final class UserEating extends Identified<String> {

   private final User user;
   private final EatingData eatingData;

   public UserEating(User user, EatingData eatingData) {
      this.user = user;
      this.eatingData = eatingData;
   }

   public User getUser() {
      return user;
   }

   public EatingData getEatingData() {
      return eatingData;
   }

   @Override
   public String getId() {
      return this.eatingData.getCardCode();
   }
}
