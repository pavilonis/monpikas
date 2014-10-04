package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class PupilFilter implements Filter {
   private String text;
   private boolean dinnerPermitted;
   private boolean breakfastPermitted;

   public PupilFilter(String text, boolean breakfastPermitted, boolean dinnerPermitted) {
      this.text = text.toLowerCase();
      this.breakfastPermitted = breakfastPermitted;
      this.dinnerPermitted = dinnerPermitted;
   }

   public String getText() {
      return text;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      String stack = item.getItemProperty("firstName").getValue().toString().toLowerCase() +
            item.getItemProperty("lastName").getValue().toString().toLowerCase() +
            (item.getItemProperty("birthDate").getValue() == null ? "" : item.getItemProperty("birthDate").getValue().toString());
      boolean itemDinnerPermission = (boolean) item.getItemProperty("dinnerPermitted").getValue();
      boolean itemBreakfastPermission = (boolean) item.getItemProperty("breakfastPermitted").getValue();
      return stack.contains(text) &&
            (!dinnerPermitted || itemDinnerPermission) &&
            (!breakfastPermitted || itemBreakfastPermission);
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}