package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.springframework.context.ApplicationContext;
import ru.xpoft.vaadin.SpringApplicationContext;

public class PupilFilter implements Filter {
   private String text;
   private boolean dinnerPermitted;
   private boolean hadDinnerToday;
   private PupilService service = SpringApplicationContext.getApplicationContext().getBean(PupilService.class);

   public PupilFilter(String text, boolean dinnerPermitted, boolean hadDinnerToday) {
      this.text = text.toLowerCase();
      this.dinnerPermitted = dinnerPermitted;
      this.hadDinnerToday = hadDinnerToday;
   }

   public String getText() {
      return text;
   }

   public boolean isDinnerPermitted() {
      return dinnerPermitted;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      String stack = item.getItemProperty("firstName").getValue().toString().toLowerCase() +
            item.getItemProperty("lastName").getValue().toString().toLowerCase() +
            (item.getItemProperty("birthDate").getValue()==null ? "" : item.getItemProperty("birthDate").getValue().toString());
      boolean itemDinnerPermission = (boolean) item.getItemProperty("dinnerPermitted").getValue();
      return stack.contains(text)
            && (!dinnerPermitted || itemDinnerPermission)
            && (!hadDinnerToday || service.hadDinnerToday((long)itemId));
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}