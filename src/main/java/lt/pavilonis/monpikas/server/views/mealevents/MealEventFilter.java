package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.service.MealService;

import java.util.Date;

import static ru.xpoft.vaadin.SpringApplicationContext.getApplicationContext;

public class MealEventFilter implements Filter {
   private String text;
   private boolean hadDinnerToday;
   private MealService service = getApplicationContext().getBean(MealService.class);

   public MealEventFilter(String text, boolean hadDinnerToday) {
      this.text = text.toLowerCase();
      this.hadDinnerToday = hadDinnerToday;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

      MealEvent event = ((BeanItem<MealEvent>) item).getBean();

      String s = event.getName() + event.getGrade() + event.getCardId() + event.getDate() + event.getPrice();

      return s.toLowerCase().contains(text)
            && (!hadDinnerToday || service.sameDay(event.getDate(), new Date()));
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}