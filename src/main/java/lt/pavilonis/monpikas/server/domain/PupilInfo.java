package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class PupilInfo {

   public PupilInfo() {
   }

   public PupilInfo(long cardId) {
      this.cardId = cardId;
   }

   @Id
   private long cardId;

   @Size(min = 1, max = 3)
   @NotNull
   private String grade;

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

   public void setCardId(long cardId) {
      this.cardId = cardId;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
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

   @Override
   public String toString() {
      return String.valueOf(cardId);
   }
}
