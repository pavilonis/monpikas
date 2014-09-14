package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class PupilFilter implements Filter {
   //private String propertyId;
   private String text;
   private boolean dinner;
   private boolean dinnerToday;

   public PupilFilter(String text, boolean dinner, boolean dinnerToday) {
      //this.propertyId = propertyId;
      this.text = text.toLowerCase();
      this.dinner = dinner;
      this.dinnerToday = dinnerToday;
   }

   public String getText() {
      return text;
   }

   public boolean isDinner() {
      return dinner;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      String stack = item.getItemProperty("firstName").getValue().toString().toLowerCase() +
            item.getItemProperty("lastName").getValue().toString().toLowerCase() +
            (item.getItemProperty("birthDate").getValue()==null ? "" : item.getItemProperty("birthDate").getValue().toString());
      boolean itemDinnerPermission = (boolean) item.getItemProperty("dinner").getValue();
      return stack.contains(text) && (!dinner || itemDinnerPermission);
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;//propertyId != null && propertyId.equals(this.propertyId);
   }
}