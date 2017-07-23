package lt.pavilonis.cmm.warehouse.receipt;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.product.Product;

import java.math.BigDecimal;

public class ReceiptItem extends Identified<Long> {

   private Product product;
   private BigDecimal unitPrice;
   private BigDecimal quantity;

   public ReceiptItem() {/**/}

   public ReceiptItem(Product product, BigDecimal unitPrice, BigDecimal quantity) {
      this(null, product, unitPrice, quantity);
   }

   public ReceiptItem(Long id, Product product, BigDecimal unitPrice, BigDecimal quantity) {
      this.setId(id);
      this.product = product;
      this.unitPrice = unitPrice;
      this.quantity = quantity;
   }

   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public BigDecimal getUnitPrice() {
      return unitPrice;
   }

   public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
   }

   public BigDecimal getCost() {
      return getQuantity().multiply(getUnitPrice());
//      return product.getMeasureUnit() == MeasureUnit.PIECE
//            ? getQuantity().multiply(getUnitPrice())
//            : getQuantity().multiply(getUnitPrice().divide(new BigDecimal(1000), BigDecimal.ROUND_HALF_UP));
   }
}
