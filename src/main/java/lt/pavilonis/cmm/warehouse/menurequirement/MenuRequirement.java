package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class MenuRequirement extends Identified<Long> {

   @NotNull
   private LocalDate date;

//   @NotNull
//   @NotEmpty
//   /**
//    * Map of tech-card-set to number
//    * (number of tech-card-sets in menu requirement)
//    */
//   private Map<TechCardSet, Integer> techCardSetNumber = new HashMap<>();

   private Collection<TechCardSetNumber> techCardSets = new ArrayList<>();

   public MenuRequirement(long id, LocalDate date) {
      this.setId(id);
      this.date = date;
   }

   public MenuRequirement() {
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public Collection<TechCardSetNumber> getTechCardSets() {
      return techCardSets;
   }

   public void setTechCardSets(Collection<TechCardSetNumber> techCardSets) {
      this.techCardSets = techCardSets;
   }


   //   public Map<TechCardSet, Integer> getTechCardSetNumber() {
//      return techCardSetNumber;
//   }

//   //Used by Vaadin binder
//   @SuppressWarnings("unused")
//   public void setTechCardSetNumber(Map<TechCardSet, Integer> techCardSetNumber) {
//      this.techCardSetNumber = techCardSetNumber;
//   }

   public int getCaloricity() {
      return techCardSets
            .stream()
            .map(TechCardSetNumber::getTechCardSet)
            .map(TechCardSet::getTechCards)
            .flatMap(Collection::stream)
            .mapToInt(TechCard::getCaloricity)
            .sum();
   }
}
