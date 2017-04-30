package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public final class EatingEvent implements Comparable, Identifiable<Long> {

   public EatingEvent(Long id,
                      String cardCode,
                      String name,
                      String grade,
                      Date date,
                      BigDecimal price,
                      EatingType eatingType,
                      PupilType pupilType) {
      this.id = id;
      this.cardCode = cardCode;
      this.name = name;
      this.grade = grade;
      this.date = date;
      this.price = price;
      this.eatingType = eatingType;
      this.pupilType = pupilType;
   }

   public EatingEvent() {
   }

   private Long id;

   @NotBlank
   private String cardCode;

   private String name;
   private String grade;

   @NotNull
   private Date date;
   private BigDecimal price;
   private EatingType eatingType;
   private PupilType pupilType;

   @Override
   public Long getId() {
      return id;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getName() {
      return name;
   }

   public String getGrade() {
      return grade;
   }

   public Date getDate() {
      return date;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public EatingType getEatingType() {
      return eatingType;
   }

   public PupilType getPupilType() {
      return pupilType;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setCardCode(String cardCode) {
      this.cardCode = cardCode;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public void setEatingType(EatingType eatingType) {
      this.eatingType = eatingType;
   }

   public void setPupilType(PupilType pupilType) {
      this.pupilType = pupilType;
   }

   @Override
   public int compareTo(Object o) {
      return ((EatingEvent) o).getDate().compareTo(this.date);
   }

   @Override
   public String toString() {
      return name + " " + date + " " + price;
   }
}
