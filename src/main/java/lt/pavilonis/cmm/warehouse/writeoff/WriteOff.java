package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.common.Identified;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public final class WriteOff extends Identified<Long> {

   private LocalDate periodStart;
   private LocalDate periodEnd;
   private Collection<WriteOffItem> items = new ArrayList<>();
   private LocalDateTime created;

   public WriteOff() {/**/}

   public WriteOff(LocalDate periodStart, LocalDate periodEnd) {
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
   }

   public WriteOff(long id, LocalDate periodStart, LocalDate periodEnd, LocalDateTime created) {
      this.setId(id);
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
   }

   public LocalDate getPeriodStart() {
      return periodStart;
   }

   public void setPeriodStart(LocalDate periodStart) {
      this.periodStart = periodStart;
   }

   public LocalDate getPeriodEnd() {
      return periodEnd;
   }

   public void setPeriodEnd(LocalDate periodEnd) {
      this.periodEnd = periodEnd;
   }

   public Collection<WriteOffItem> getItems() {
      return items;
   }

   public void setItems(Collection<WriteOffItem> items) {
      this.items = items;
   }

   public LocalDateTime getCreated() {
      return created;
   }

   public void setCreated(LocalDateTime created) {
      this.created = created;
   }
}
