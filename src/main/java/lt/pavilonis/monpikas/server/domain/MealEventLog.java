package lt.pavilonis.monpikas.server.domain;

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
public class MealEventLog implements Comparable {

   public MealEventLog() {
   }

   public MealEventLog(long cardId, String name, String grade, Date date,
                       BigDecimal price, MealType mealType, PupilType pupilType) {
      this.cardId = cardId;
      this.name = name;
      this.grade = grade;
      this.date = date;
      this.price = price;
      this.mealType = mealType;
      this.pupilType = pupilType;
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
   private MealType mealType;

   @Enumerated(EnumType.STRING)
   private PupilType pupilType;

   @Transient
   private Optional<Pupil> pupil = empty();

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

   public MealType getMealType() {
      return mealType;
   }

   public void setMealType(MealType type) {
      this.mealType = type;
   }

   public PupilType getPupilType() {
      return pupilType;
   }

   public void setPupilType(PupilType pupilType) {
      this.pupilType = pupilType;
   }

   @Override
   public int compareTo(Object o) {
      return ((MealEventLog) o).getDate().compareTo(this.date);
   }

   public Optional<Pupil> getPupil() {
      return pupil;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public void setPupil(Optional<Pupil> pupil) {
      this.pupil = pupil;
   }

   @Override
   public String toString() {
      return name + " " + date + " " + price;
   }
}
