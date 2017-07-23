package lt.pavilonis.cmm.warehouse.receipt;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public final class Receipt extends Identified<Long> {

   private long number;
   private Supplier supplier;
   private Date dateTime;
   private Collection<ReceiptItem> items = new ArrayList<>();

   public Receipt() {/**/}

   public Receipt(long id, Supplier supplier, Date dateTime, Collection<ReceiptItem> items) {
      this.setId(id);
      this.number = id;
      this.supplier = supplier;
      this.dateTime = dateTime;
      this.items = items;
   }


   public long getNumber() {
      return number;
   }

   public Supplier getSupplier() {
      return supplier;
   }

   public void setSupplier(Supplier supplier) {
      this.supplier = supplier;
   }

   public Date getDateTime() {
      return dateTime;
   }

   public void setDateTime(Date dateTime) {
      this.dateTime = dateTime;
   }

   public BigDecimal getSum() {
      return items.stream()
            .map(ReceiptItem::getCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
   }

   public Collection<ReceiptItem> getItems() {
      return items;
   }

   public void setItems(Collection<ReceiptItem> items) {
      this.items = items;
   }
}
