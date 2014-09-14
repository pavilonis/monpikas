package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class DinnerEvent {

   @Id
   @GeneratedValue
   private String id;

   @ManyToOne
   private PupilInfo pupil;

   private LocalDateTime time;


   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public LocalDateTime getTime() {
      return time;
   }

   public void setTime(LocalDateTime time) {
      this.time = time;
   }

   public PupilInfo getPupil() {
      return pupil;
   }

   public void setPupil(PupilInfo pupil) {
      this.pupil = pupil;
   }
}
