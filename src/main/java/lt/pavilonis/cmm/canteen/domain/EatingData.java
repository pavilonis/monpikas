package lt.pavilonis.cmm.canteen.domain;

import java.util.HashSet;
import java.util.Set;

public final class EatingData {
   private String cardCode;
   private PupilType type = PupilType.SOCIAL;
   private String comment;
   private Set<Eating> eatings = new HashSet<>();

   public EatingData(String cardCode, PupilType type, String comment, Set<Eating> eatings) {
      this.cardCode = cardCode;
      this.type = type;
      this.comment = comment;
      this.eatings = eatings;
   }

   public EatingData(String cardCode) {
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

   public Set<Eating> getEatings() {
      return eatings;
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

   public void setEatings(Set<Eating> eatings) {
      this.eatings = eatings;
   }
}
