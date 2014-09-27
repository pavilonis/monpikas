package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class PupilFilter implements Filter {
   private String text;
   private boolean dinnerPermitted;
   private boolean hadDinnerToday;

   public PupilFilter(String text, Boolean dinnerPermitted) {
      this.text = text.toLowerCase();
      this.dinnerPermitted = dinnerPermitted;
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
            (item.getItemProperty("birthDate").getValue() == null ? "" : item.getItemProperty("birthDate").getValue().toString());
      boolean itemDinnerPermission = (boolean) item.getItemProperty("dinnerPermitted").getValue();
      return stack.contains(text) && (!dinnerPermitted || itemDinnerPermission);
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}