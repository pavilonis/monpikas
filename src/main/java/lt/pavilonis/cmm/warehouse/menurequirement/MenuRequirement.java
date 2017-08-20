package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuRequirement extends Identified<Long> {

   @NotNull
   private LocalDate date;

   @NotNull
   @NotEmpty
   private Collection<TechCardSet> techCardSets = new ArrayList<>();

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

   public Collection<TechCardSet> getTechCardSets() {
      return techCardSets;
   }

   //Used by Vaadin binder
   @SuppressWarnings("unused")
   public void setTechCardSets(Collection<TechCardSet> techCardSets) {
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
