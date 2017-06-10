package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class MenuRequirement extends Identified<Long> {

   private LocalDate date;
   private List<TechCardSet> techCardSets;

   public MenuRequirement(long id, LocalDate date, List<TechCardSet> techCardSets) {
      this.setId(id);
      this.date = date;
      this.techCardSets = techCardSets;
   }

   public MenuRequirement() {
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public List<TechCardSet> getTechCardSets() {
      return techCardSets;
   }

   public void setTechCardSets(List<TechCardSet> techCardSets) {
      this.techCardSets = techCardSets;
   }

   public int getCaloricity() {
      return techCardSets.stream()
            .map(TechCardSet::getTechCards)
            .flatMap(Collection::stream)
            .mapToInt(TechCard::getCaloricity)
            .sum();
   }
}
