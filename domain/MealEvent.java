package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class MealEvent {

   @Id
   @GeneratedValue
   private long id;

   private long cardId;

   private String name;

   private Date date;

   public MealEvent() {
   }

   public MealEvent(long cardId, String name, Date date) {
      this.cardId = cardId;
      this.name = name;
      this.date = date;
   }

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
}
