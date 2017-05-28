package lt.pavilonis.cmm.warehouse.product;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

public final class Product extends Named<Long> {

   private MeasureUnit measureUnit;
   private int unitWeight;
   private ProductGroup productGroup;

   public Product() {
   }

   public Product(long id, String name, MeasureUnit measureUnit,
                  int unitWeight, ProductGroup productGroup) {
      setId(id);
      setName(name);
      this.measureUnit = measureUnit;
      this.unitWeight = unitWeight;
      this.productGroup = productGroup;
   }

   public MeasureUnit getMeasureUnit() {
      return measureUnit;
   }

   public void setMeasureUnit(MeasureUnit measureUnit) {
      this.measureUnit = measureUnit;
   }

   public int getUnitWeight() {
      return unitWeight;
   }

   public void setUnitWeight(int unitWeight) {
      this.unitWeight = unitWeight;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public void setProductGroup(ProductGroup productGroup) {
      this.productGroup = productGroup;
   }
}
