package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;

import java.math.BigDecimal;

public class WriteOffItem extends Identified<Long> {

   private ReceiptItem receiptItem;
   private BigDecimal quantity;

   public WriteOffItem() {/**/}

   public WriteOffItem(ReceiptItem receiptItem, BigDecimal quantity) {
      this.receiptItem = receiptItem;
      this.quantity = quantity;
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
}
