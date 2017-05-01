package lt.pavilonis.cmm.warehouse.dish;

import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.warehouse.dishGroup.DishGroup;

public final class Dish extends Named<Long> {

   private DishGroup dishGroup;

   public Dish() {
   }

   public Dish(long id, String name, DishGroup dishGroup) {
      setId(id);
      setName(name);
      this.dishGroup = dishGroup;
   }

   public DishGroup getDishGroup() {
      return dishGroup;
   }

   public void setDishGroup(DishGroup dishGroup) {
      this.dishGroup = dishGroup;
   }
}
