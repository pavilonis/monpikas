package lt.pavilonis.cmm.warehouse.productgroup;

import lt.pavilonis.cmm.common.Identifiable;

public class ProductGroup implements Identifiable<Long> {

   private Long id;
   private String name;
   private int kcal100;

   public ProductGroup() {
   }

   public ProductGroup(Long id, String name, int kcal100) {
      this.id = id;
      this.name = name;
      this.kcal100 = kcal100;
   }

   @Override
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getKcal100() {
      return kcal100;
   }

   public void setKcal100(int kcal100) {
      this.kcal100 = kcal100;
   }
}
