package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import lt.pavilonis.monpikas.server.service.MealService;
import ru.xpoft.vaadin.SpringApplicationContext;

import java.util.Date;

public class MealEventFilter implements Filter {
   private String text;
   private boolean hadDinnerToday;
   private MealService service = SpringApplicationContext.getApplicationContext().getBean(MealService.class);

   public MealEventFilter(String text, boolean hadDinnerToday) {
      this.text = text.toLowerCase();
      this.hadDinnerToday = hadDinnerToday;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      String stack = item.getItemProperty("name").getValue().toString().toLowerCase();
      Date date = (Date)item.getItemProperty("date").getValue();
      return stack.contains(text) && (!hadDinnerToday || service.sameDay(date, new Date()));
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}