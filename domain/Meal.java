package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;

@Entity
public class Meal implements Serializable {

   public Meal() {
   }

   @Id
   @GeneratedValue
   private Long id;

   @Size(min = 2)
   @NotNull
   private String name;

   @NotNull
   @Enumerated(EnumType.STRING)
   private MealType type;

   @DecimalMin("0")
   @DecimalMax("99")
   @NotNull
   private BigDecimal price;

   private LocalTime startTime;

   private LocalTime endTime;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public MealType getType() {
      return type;
   }

   public void setType(MealType type) {
      this.type = type;
   }

   public LocalTime getStartTime() {
      return startTime;
   }

   public void setStartTime(LocalTime startTime) {
      this.startTime = startTime;
   }

   public LocalTime getEndTime() {
      return endTime;
   }

   public void setEndTime(LocalTime endTime) {
      this.endTime = endTime;
   }

   @Override
   public String toString() {
      return name + " - " + new DecimalFormat("0.00").format(price);
   }
}
