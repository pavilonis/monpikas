package lt.pavilonis.cmm.warehouse.meal;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.mealtype.MealType;
import lt.pavilonis.cmm.warehouse.techcard.TechnologicalCard;

import java.util.List;

public class Meal extends Identified<Long> {

   private MealType type;
   private List<TechnologicalCard> technologicalCards;
//   private MenuRequirement menuRequirement;

   public Meal(long id, MealType type, List<TechnologicalCard> technologicalCards) {
      this.setId(id);
      this.type = type;
      this.technologicalCards = technologicalCards;
   }

   public MealType getType() {
      return type;
   }

   public void setType(MealType type) {
      this.type = type;
   }

   public List<TechnologicalCard> getTechnologicalCards() {
      return technologicalCards;
   }

   public void setTechnologicalCards(List<TechnologicalCard> technologicalCards) {
      this.technologicalCards = technologicalCards;
   }
}
