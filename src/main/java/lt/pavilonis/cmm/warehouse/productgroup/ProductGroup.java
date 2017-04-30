package lt.pavilonis.cmm.warehouse.productgroup;

import lt.pavilonis.cmm.common.Named;

public final class ProductGroup extends Named<Long> {

   private int kcal100;

   public ProductGroup() {
   }

   public ProductGroup(Long id, String name, int kcal100) {
      setId(id);
      setName(name);
      this.kcal100 = kcal100;
   }

   public int getKcal100() {
      return kcal100;
   }

   public void setKcal100(int kcal100) {
      this.kcal100 = kcal100;
   }
}
