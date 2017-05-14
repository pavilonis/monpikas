package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroup;

public final class TechnologicalCard extends Named<Long> {

   private TechnologicalCardGroup technologicalCardGroup;

   public TechnologicalCard() {
   }

   public TechnologicalCard(long id, String name, TechnologicalCardGroup technologicalCardGroup) {
      setId(id);
      setName(name);
      this.technologicalCardGroup = technologicalCardGroup;
   }

   public TechnologicalCardGroup getTechnologicalCardGroup() {
      return technologicalCardGroup;
   }

   public void setTechnologicalCardGroup(TechnologicalCardGroup technologicalCardGroup) {
      this.technologicalCardGroup = technologicalCardGroup;
   }
}
