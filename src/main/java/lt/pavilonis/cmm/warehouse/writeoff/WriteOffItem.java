package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;

import java.math.BigDecimal;

public class WriteOffItem extends Identified<Long> {

   private ReceiptItem receiptItem;
   private BigDecimal quantityAvailableBefore;
   private BigDecimal quantityConsumed;
   private BigDecimal quantity; // writeOff
   private BigDecimal QuantityAvailableAfter;
   private ProductGroup productGroup;

   public WriteOffItem() {/**/}

   public WriteOffItem(ReceiptItem receiptItem, BigDecimal quantityAvailableBefore,
                       BigDecimal quantityConsumed, BigDecimal quantity,
                       BigDecimal QuantityAvailableAfter, ProductGroup productGroup) {

      this.receiptItem = receiptItem;
      this.quantityAvailableBefore = quantityAvailableBefore;
      this.quantityConsumed = quantityConsumed;
      this.quantity = quantity;
      this.QuantityAvailableAfter = QuantityAvailableAfter;
      this.productGroup = productGroup;
   }

   public WriteOffItem(long id, ReceiptItem receiptItem, BigDecimal quantity) {
      this.setId(id);
      this.receiptItem = receiptItem;
      this.quantity = quantity;
   }

   public ReceiptItem getReceiptItem() {
      return receiptItem;
   }

   public void setReceiptItem(ReceiptItem receiptItem) {
      this.receiptItem = receiptItem;
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
   }

   public BigDecimal getUnitPrice() {
      return receiptItem == null
            ? BigDecimal.ZERO
            : receiptItem.getUnitPrice();
   }

   public BigDecimal getCost() {
      return receiptItem == null
            ? BigDecimal.ZERO
            : receiptItem.getUnitPrice().multiply(quantity);
   }

   public BigDecimal getQuantityAvailableBefore() {
      return quantityAvailableBefore;
   }

   public void setQuantityAvailableBefore(BigDecimal quantityAvailableBefore) {
      this.quantityAvailableBefore = quantityAvailableBefore;
   }

   public BigDecimal getQuantityAvailableAfter() {
      return QuantityAvailableAfter;
   }

   public void setQuantityAvailableAfter(BigDecimal quantityAvailableAfter) {
      this.QuantityAvailableAfter = quantityAvailableAfter;
   }

   public BigDecimal getQuantityConsumed() {
      return quantityConsumed;
   }

   public void setQuantityConsumed(BigDecimal quantityConsumed) {
      this.quantityConsumed = quantityConsumed;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public void setProductGroup(ProductGroup productGroup) {
      this.productGroup = productGroup;
   }
}
