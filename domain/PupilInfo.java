package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class PupilInfo {

   public PupilInfo() {
   }

   public PupilInfo(long cardId, boolean breakfastPermitted, boolean dinnerPermitted, String comment) {
      this.cardId = cardId;
      this.dinnerPermitted = dinnerPermitted;
      this.comment = comment;
      this.breakfastPermitted = breakfastPermitted;
   }

   @Id
   private long cardId;

   private boolean dinnerPermitted;

   private boolean breakfastPermitted;

   @Lob
   private String comment;

   @ManyToOne
   @JoinColumn(name = "breakfastPortion_id")
   private Portion breakfastPortion;

   @ManyToOne
   @JoinColumn(name = "dinnerPortion_id")
   private Portion dinnerPortion;

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

   public boolean isBreakfastPermitted() {
      return breakfastPermitted;
   }

   public void setBreakfastPermitted(boolean breakfastPermitted) {
      this.breakfastPermitted = breakfastPermitted;
   }

   public Portion getBreakfastPortion() {
      return breakfastPortion;
   }

   public void setBreakfastPortion(Portion breakfastPortion) {
      this.breakfastPortion = breakfastPortion;
   }

   public Portion getDinnerPortion() {
      return dinnerPortion;
   }

   public void setDinnerPortion(Portion dinnerPortion) {
      this.dinnerPortion = dinnerPortion;
   }
}
