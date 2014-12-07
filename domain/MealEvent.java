package lt.pavilonis.monpikas.server.domain;

import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static java.util.Optional.empty;

@Entity
public class MealEvent implements Comparable {

   public MealEvent() {
   }

   public MealEvent(long cardId, String name, String grade, Date date, BigDecimal price, PortionType type) {
      this.cardId = cardId;
      this.name = name;
      this.grade = grade;
      this.date = date;
      this.price = price;
      this.type = type;
   }

   @Id
   @GeneratedValue
   private long id;

   private long cardId;

   private String name;

   private String grade;

   private Date date;

   private BigDecimal price;

   @Enumerated(EnumType.STRING)
   private PortionType type;

   @Transient
   private Optional<PupilInfo> info = empty();

   public Date getDate() {
      return date;
   }

   public void setDate(Date time) {
      this.date = time;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long cardId) {
      this.cardId = cardId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public PortionType getType() {
      return type;
   }

   public void setType(PortionType type) {
      this.type = type;
   }

   @Override
   public int compareTo(Object o) {
      return ((MealEvent) o).getDate().compareTo(this.date);
   }

   public Optional<PupilInfo> getInfo() {
      return info;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public void setInfo(Optional<PupilInfo> info) {
      this.info = info;
   }
}
