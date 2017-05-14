package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class TechCard extends Named<Long> {

   private TechCardGroup group;
   private Map<ProductGroup, BigDecimal> productGroupOutputWeight = new HashMap<>();

   public TechCard() {
   }

   public TechCard(long id, String name, TechCardGroup group,
                   Map<ProductGroup, BigDecimal> productGroupOutputWeight) {

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

   public Map<ProductGroup, BigDecimal> getProductGroupOutputWeight() {
      return productGroupOutputWeight;
   }

   public void setProductGroupOutputWeight(Map<ProductGroup, BigDecimal> productGroupOutputWeight) {
      this.productGroupOutputWeight = productGroupOutputWeight;
   }
}
