package lt.pavilonis.cmm.canteen.ui.user;

import lt.pavilonis.cmm.canteen.domain.EatingType;

public final class UserEatingFilter {
   private final EatingType eatingType;
   private final String text;
   private final boolean withEatingAssigned;

   public UserEatingFilter() {
      this(null, null, false);
   }

   public UserEatingFilter(EatingType eatingType, String text, boolean withEatingAssigned) {
      this.eatingType = eatingType;
      this.text = text;
      this.withEatingAssigned = withEatingAssigned;
   }

   public EatingType getEatingType() {
      return eatingType;
   }

   public String getText() {
      return text;
   }

   public boolean isWithEatingAssigned() {
      return withEatingAssigned;
   }
}
