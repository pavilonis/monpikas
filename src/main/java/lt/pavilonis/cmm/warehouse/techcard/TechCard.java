package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroup;

import java.util.HashMap;
import java.util.Map;

public final class TechCard extends Named<Long> {

   private TechCardGroup group;
   private Map<ProductGroup, Integer> productGroupOutputWeight = new HashMap<>();

   public TechCard() {
   }

   public TechCard(long id, String name, TechCardGroup group,
                   Map<ProductGroup, Integer> productGroupOutputWeight) {

      setId(id);
      setName(name);
      this.group = group;
      this.productGroupOutputWeight = productGroupOutputWeight;
   }

   public TechCardGroup getGroup() {
      return group;
   }

   public void setGroup(TechCardGroup group) {
      this.group = group;
   }

   public Map<ProductGroup, Integer> getProductGroupOutputWeight() {
      return productGroupOutputWeight;
   }

   public void setProductGroupOutputWeight(Map<ProductGroup, Integer> productGroupOutputWeight) {
      this.productGroupOutputWeight = productGroupOutputWeight;
   }

   @SuppressWarnings("unused")
   public int getCaloricity() {
      return productGroupOutputWeight.entrySet().stream()
            .mapToInt(entry -> {
               int kcal100 = entry.getKey().getKcal100();
               int outputWeight = entry.getValue();
               Double result = outputWeight / 100d * kcal100;
               return result.intValue();
            }).sum();
   }
}
