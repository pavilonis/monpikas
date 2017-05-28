package lt.pavilonis.cmm.warehouse.meal;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.mealtype.MealType;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;

import java.util.List;

public class Meal extends Identified<Long> {

   private MealType type;
   private List<TechCard> techCards;
//   private MenuRequirement menuRequirement;

   public Meal(long id, MealType type, List<TechCard> techCards) {
      this.setId(id);
      this.type = type;
      this.techCards = techCards;
   }

   public MealType getType() {
      return type;
   }

   public void setType(MealType type) {
      this.type = type;
   }

   public List<TechCard> getTechCards() {
      return techCards;
   }

   public void setTechCards(List<TechCard> techCards) {
      this.techCards = techCards;
   }
}
