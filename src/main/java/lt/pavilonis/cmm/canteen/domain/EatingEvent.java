package lt.pavilonis.cmm.canteen.domain;

import lt.pavilonis.cmm.common.Named;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public final class EatingEvent extends Named<Long> implements Comparable {

   public EatingEvent(Long id,
                      String cardCode,
                      String name,
                      String grade,
                      Date date,
                      BigDecimal price,
                      EatingType eatingType,
                      PupilType pupilType) {
      setId(id);
      this.cardCode = cardCode;
      setName(name);
      this.grade = grade;
      this.date = date;
      this.price = price;
      this.eatingType = eatingType;
      this.pupilType = pupilType;
   }

   public EatingEvent() {
   }

   @NotBlank
   private String cardCode;

   private String grade;

   @NotNull
   private Date date;
   private BigDecimal price;
   private EatingType eatingType;
   private PupilType pupilType;

   public String getCardCode() {
      return cardCode;
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

   public void setCardCode(String cardCode) {
      this.cardCode = cardCode;
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
      return getName() + " " + date + " " + price;
   }
}
