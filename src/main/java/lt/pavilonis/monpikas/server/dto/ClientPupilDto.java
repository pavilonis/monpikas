package lt.pavilonis.monpikas.server.dto;

import lt.pavilonis.monpikas.server.domain.Meal;

import java.io.Serializable;

public class ClientPupilDto implements Serializable {

   private long id;
   private String name;
   private Meal meal;
   private String grade;

   public ClientPupilDto() {
   }

   public ClientPupilDto(long id, String name, Meal meal, String grade) {
      this.id = id;
      this.name = name;
      this.meal = meal;
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

   public Meal getMeal() {
      return meal;
   }

   public void setMeal(Meal meal) {
      this.meal = meal;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }
}
