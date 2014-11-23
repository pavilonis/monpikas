package lt.pavilonis.monpikas.server.domain;

import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.DecimalFormat;

@Entity
public class Portion implements Serializable {

   public Portion() {
   }

   @Id
   @GeneratedValue
   private Long id;

   @Size(min = 2)
   @NotNull
   private String name;

   @NotNull
   @Enumerated(EnumType.STRING)
   private PortionType type;

   @Min(0)
   @NotNull
   private double price;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Double getPrice() {
      return price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public PortionType getType() {
      return type;
   }

   public void setType(PortionType type) {
      this.type = type;
   }

   @Override
   public String toString() {
      return name + " - " + new DecimalFormat("0.00").format(price);
   }
}
