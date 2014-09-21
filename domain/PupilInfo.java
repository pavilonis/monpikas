package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PupilInfo {

   public PupilInfo() {
   }

   public PupilInfo(long cardId, boolean dinnerPermitted, String comment) {
      this.cardId = cardId;
      this.dinnerPermitted = dinnerPermitted;
      this.comment = comment;
   }

   @Id
   private long cardId;

   private boolean dinnerPermitted;

   @Lob
   private String comment;

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long adbUserId) {
      this.cardId = adbUserId;
   }

   public boolean isDinnerPermitted() {
      return dinnerPermitted;
   }

   public void setDinnerPermitted(boolean dinnerPermission) {
      this.dinnerPermitted = dinnerPermission;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
