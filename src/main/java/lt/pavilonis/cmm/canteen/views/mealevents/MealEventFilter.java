package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MealEventFilter implements Filter {
   private MealService service;
   private String text;
   private boolean hadDinnerToday;

   public MealEventFilter(MealService service, String text, boolean hadDinnerToday) {
      this.service = service;
      this.text = text.toLowerCase();
      this.hadDinnerToday = hadDinnerToday;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

      @SuppressWarnings("unchecked")
      MealEventLog event = ((BeanItem<MealEventLog>) item).getBean();

      String s = event.getName() + event.getGrade() + event.getCardCode() + event.getDate() + event.getPrice();

      return s.toLowerCase().contains(text)
            && (!hadDinnerToday || service.sameDay(event.getDate(), new Date()));
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}