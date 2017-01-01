package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.Pupil;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

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

      Pupil pupil = ((BeanItem<Pupil>) item).getBean();

      Collection<Object> pupilData = asList(
            pupil.getFirstName(),
            pupil.getLastName(),
            pupil.getBirthDate() == null ? "" : pupil.getBirthDate().toString(),
            pupil.getGrade(),
            pupil.getCardCode()
      );

      return pupilData.stream()

            .anyMatch(element -> containsIgnoreCase(String.valueOf(element), text))

            && mealType == null || pupil.getMeals().stream().anyMatch(meal -> meal.getType() == mealType);
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}