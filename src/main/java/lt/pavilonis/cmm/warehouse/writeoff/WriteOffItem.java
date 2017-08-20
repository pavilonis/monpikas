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
   private BigDecimal quantityAvailableAfter;
   private ProductGroup productGroup;

   public WriteOffItem() {/**/}

   public WriteOffItem(ReceiptItem receiptItem, BigDecimal quantityAvailableBefore,
                       BigDecimal quantityConsumed, BigDecimal quantity,
                       BigDecimal quantityAvailableAfter, ProductGroup productGroup) {

      this(null, receiptItem, quantityAvailableBefore, quantityConsumed,
            quantity, quantityAvailableAfter, productGroup);
   }

   public WriteOffItem(Long id, ReceiptItem receiptItem, BigDecimal quantityAvailableBefore,
                       BigDecimal quantityConsumed, BigDecimal quantity,
                       BigDecimal quantityAvailableAfter, ProductGroup productGroup) {

      this.setId(id);
      this.receiptItem = receiptItem;
      this.quantityAvailableBefore = quantityAvailableBefore;
      this.quantityConsumed = quantityConsumed;
      this.quantity = quantity;
      this.quantityAvailableAfter = quantityAvailableAfter;
      this.productGroup = productGroup;
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
      return quantityAvailableAfter;
   }

   public void setQuantityAvailableAfter(BigDecimal quantityAvailableAfter) {
      this.quantityAvailableAfter = quantityAvailableAfter;
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
