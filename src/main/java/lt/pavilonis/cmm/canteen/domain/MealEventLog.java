package lt.pavilonis.cmm.canteen.domain;

import java.math.BigDecimal;
import java.util.Date;

public final class MealEventLog implements Comparable {

   public MealEventLog(Long id,
                       String cardCode,
                       String name,
                       String grade,
                       Date date,
                       BigDecimal price,
                       MealType mealType,
                       PupilType pupilType) {
      this.id = id;
      this.cardCode = cardCode;
      this.name = name;
      this.grade = grade;
      this.date = date;
      this.price = price;
      this.mealType = mealType;
      this.pupilType = pupilType;
   }

   private final Long id;
   private final String cardCode;
   private final String name;
   private final String grade;
   private final Date date;
   private final BigDecimal price;
   private final MealType mealType;
   private final PupilType pupilType;

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

   public MealType getMealType() {
      return mealType;
   }

   public PupilType getPupilType() {
      return pupilType;
   }

   @Override
   public int compareTo(Object o) {
      return ((MealEventLog) o).getDate().compareTo(this.date);
   }

   @Override
   public String toString() {
      return name + " " + date + " " + price;
   }
}
