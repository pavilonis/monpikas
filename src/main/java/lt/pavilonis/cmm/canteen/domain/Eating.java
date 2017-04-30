package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.converter.ADecimalFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Objects;

public final class Eating extends Named<Long> {

   @NotNull
   private EatingType type;

   @NotNull
   private BigDecimal price;

   @NotNull
   private LocalTime startTime;

   @NotNull
   private LocalTime endTime;

   public Eating() {
   }

   public Eating(Long id, String name, EatingType type, BigDecimal price, LocalTime startTime, LocalTime endTime) {
      setId(id);
      setName(name);
      this.type = type;
      this.price = price;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public EatingType getType() {
      return type;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public LocalTime getStartTime() {
      return startTime;
   }

   public LocalTime getEndTime() {
      return endTime;
   }

   public void setEndTime(LocalTime endTime) {
      this.endTime = endTime;
   }

   public void setStartTime(LocalTime startTime) {
      this.startTime = startTime;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public void setType(EatingType type) {
      this.type = type;
   }

   @Override
   public int hashCode() {
      return Math.toIntExact(getId());
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof Eating) {
         Eating eating = (Eating) obj;
         return Objects.equals(eating.hashCode(), this.hashCode());
      }
      return false;
   }
}
