package lt.pavilonis.cmm.warehouse.techcardset;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.mealtype.MealType;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;

import java.util.Set;

public final class TechCardSet extends Named<Long> {

   private MealType type;
   private Set<TechCard> techCards;
//   private MenuRequirement menuRequirement;

   public TechCardSet() {/**/}

   public TechCardSet(long id, String name, MealType type, Set<TechCard> techCards) {
      this.setId(id);
      this.setName(name);
      this.type = type;
      this.techCards = techCards;
   }

   public MealType getType() {
      return type;
   }

   public void setType(MealType type) {
      this.type = type;
   }

   public Set<TechCard> getTechCards() {
      return techCards;
   }

   public void setTechCards(Set<TechCard> techCards) {
      this.techCards = techCards;
   }
}
