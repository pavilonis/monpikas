package lt.pavilonis.monpikas.server.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;

public final class Meal {

   private Long id;
   private String name;
   private MealType type;
   private BigDecimal price;
   private LocalTime startTime;
   private LocalTime endTime;

   public Meal() {
   }

   public Meal(Long id, String name, MealType type, BigDecimal price, LocalTime startTime, LocalTime endTime) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.price = price;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public MealType getType() {
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

   public void setId(Long id) {
      this.id = id;
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

   public void setType(MealType type) {
      this.type = type;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name + " - " + new DecimalFormat("0.00").format(price);
   }
}
