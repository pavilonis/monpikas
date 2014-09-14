package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PupilInfo {

   public PupilInfo() {
   }

   public PupilInfo(long cardId, boolean dinnerPermission, String comment) {
      this.cardId = cardId;
      this.dinnerPermission = dinnerPermission;
      this.comment = comment;
   }

   @Id
   private long cardId;

   private boolean dinnerPermission;

   @Lob
   private String comment;

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long adbUserId) {
      this.cardId = adbUserId;
   }

   public boolean isDinnerPermission() {
      return dinnerPermission;
   }

   public void setDinnerPermission(boolean dinnerPermission) {
      this.dinnerPermission = dinnerPermission;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
