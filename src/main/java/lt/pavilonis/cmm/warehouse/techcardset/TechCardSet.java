package lt.pavilonis.cmm.warehouse.techcardset;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetType;

import java.util.Collection;
import java.util.HashSet;

public final class TechCardSet extends Named<Long> {

   private TechCardSetType type;
   private Collection<TechCard> techCards = new HashSet<>();
//   private MenuRequirement menuRequirement;

   public TechCardSet() {/**/}

   public TechCardSet(long id, String name, TechCardSetType type, Collection<TechCard> techCards) {
      this.setId(id);
      this.setName(name);
      this.type = type;
      this.techCards = techCards;
   }

   public TechCardSetType getType() {
      return type;
   }

   public void setType(TechCardSetType type) {
      this.type = type;
   }

   public Collection<TechCard> getTechCards() {
      return techCards;
   }

   public void setTechCards(Collection<TechCard> techCards) {
      this.techCards = techCards;
   }

   @SuppressWarnings("unused")
   public int getCaloricity() {
      return techCards.stream()
            .mapToInt(TechCard::getCaloricity)
            .sum();
   }
}
