package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.meal.Meal;

import java.time.LocalDate;
import java.util.List;

public class MenuRequirement extends Identified<Long> {

   private LocalDate date;
   private List<Meal> meals;

   public MenuRequirement(long id, LocalDate date, List<Meal> meals) {
      this.setId(id);
      this.date = date;
      this.meals = meals;
   }

   public MenuRequirement() {
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public List<Meal> getMeals() {
      return meals;
   }

   public void setMeals(List<Meal> meals) {
      this.meals = meals;
   }
}
