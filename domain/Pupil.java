package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Pupil {

   public Pupil() {
   }

   public Pupil(long cardId) {
      this.cardId = cardId;
   }

   @Id
   private long cardId;

   @Lob
   private String comment;

   @NotNull
   @Enumerated(EnumType.STRING)
   private PupilType type;

   @ManyToMany(fetch = FetchType.EAGER)
   private Set<Meal> meals = new HashSet<>();

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long cardId) {
      this.cardId = cardId;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public PupilType getType() {
      return type;
   }

   public void setType(PupilType type) {
      this.type = type;
   }

   public Set<Meal> getMeals() {
      return meals;
   }

   public void setMeals(Set<Meal> meals) {
      this.meals = meals;
   }

   @Override
   public String toString() {
      return String.valueOf(cardId);
   }
}
