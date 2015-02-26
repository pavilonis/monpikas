package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Portion;

import java.io.Serializable;

public class ClientPupilDto implements Serializable {

   private long id;
   private String name;
   private Portion portion;
   private String grade;

   public ClientPupilDto() {
   }

   public ClientPupilDto(long id, String name, Portion portion, String grade) {
      this.id = id;
      this.name = name;
      this.portion = portion;
      this.grade = grade;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Portion getPortion() {
      return portion;
   }

   public void setPortion(Portion portion) {
      this.portion = portion;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }
}
