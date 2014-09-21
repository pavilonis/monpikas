package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class DinnerEvent {

   @Id
   @GeneratedValue
   private String id;

   @ManyToOne
   @JoinColumn(name = "pupilInfo_cardId")
   private PupilInfo pupilInfo;

   private Date date;

   public DinnerEvent() {
   }

   public DinnerEvent(PupilInfo pupilInfo, Date date) {
      this.pupilInfo = pupilInfo;
      this.date = date;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date time) {
      this.date = time;
   }

   public PupilInfo getPupilInfo() {
      return pupilInfo;
   }

   public void setPupilInfo(PupilInfo pupil) {
      this.pupilInfo = pupil;
   }
}
