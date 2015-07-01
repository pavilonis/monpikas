package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.dto.PupilDto;

public class PupilFilter implements Filter {
   private String text;
   private MealType mealType;

   public PupilFilter(String text, MealType mealType) {
      this.text = text;
      this.mealType = mealType;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

      PupilDto dto = ((BeanItem<PupilDto>) item).getBean();

      String stack = dto.getFirstName().toLowerCase() + dto.getLastName().toLowerCase() +
            (dto.getBirthDate().isPresent() ? dto.getBirthDate().get() : "") +
            dto.getGrade().orElse("") + dto.getCardId();

      return stack.contains(text) &&
            (mealType == null || dto.getMeals().stream()
                  .anyMatch(dtoMeal -> dtoMeal.getType() == mealType));
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}