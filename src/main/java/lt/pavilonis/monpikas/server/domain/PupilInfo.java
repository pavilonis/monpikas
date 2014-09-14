package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class PupilInfo {

   public PupilInfo() {
   }

   @Id
   private long id;

   @Column
   private long cardId;

   private boolean dinnerPermission;

   @Lob
   private String comment;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

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
