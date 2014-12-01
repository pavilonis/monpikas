package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;

public class PupilFilter implements Filter {
   private String text;
   private boolean breakfastChecked;
   private boolean dinnerChecked;

   public PupilFilter(String text, boolean breakfastChecked, boolean dinnerChecked) {
      this.text = text;
      this.breakfastChecked = breakfastChecked;
      this.dinnerChecked = dinnerChecked;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

      AdbPupilDto dto = ((BeanItem<AdbPupilDto>) item).getBean();

      String stack = dto.getFirstName().toLowerCase() + dto.getLastName().toLowerCase() +
            (dto.getBirthDate().isPresent() ? dto.getBirthDate().get() : "") +
            dto.getGrade().orElse("") + dto.getCardId();

      return stack.contains(text) &&
            (!breakfastChecked || dto.getBreakfastPortion().isPresent())
            && (!dinnerChecked || dto.getDinnerPortion().isPresent());
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}