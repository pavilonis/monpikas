package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroup;

public final class TechnologicalCard extends Named<Long> {

   private TechnologicalCardGroup dishGroup;

   public TechnologicalCard() {
   }

   public TechnologicalCard(long id, String name, TechnologicalCardGroup dishGroup) {
      setId(id);
      setName(name);
      this.dishGroup = dishGroup;
   }

   public TechnologicalCardGroup getDishGroup() {
      return dishGroup;
   }

   public void setDishGroup(TechnologicalCardGroup dishGroup) {
      this.dishGroup = dishGroup;
   }
}
