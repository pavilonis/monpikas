package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class PupilFilter implements Container.Filter {
   private String propertyId;
   private String text;
   private boolean dinner;

   public PupilFilter(String propertyId, String text, boolean dinner) {
      this.propertyId = propertyId;
      this.text = text;
      this.dinner = dinner;
   }

   public String getText() {
      return text;
   }

   public boolean isDinner() {
      return dinner;
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      Property p = item.getItemProperty(propertyId);

      return false;
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return propertyId != null && propertyId.equals(this.propertyId);
   }
}