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

   public MealEventLog() {
   }

   private Long id;
   private String cardCode;
   private String name;
   private String grade;
   private Date date;
   private BigDecimal price;
   private MealType mealType;
   private PupilType pupilType;

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

   public void setMealType(MealType mealType) {
      this.mealType = mealType;
   }

   public void setPupilType(PupilType pupilType) {
      this.pupilType = pupilType;
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
