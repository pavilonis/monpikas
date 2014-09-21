package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class DinnerFilter implements Filter {
   private String text;

   public DinnerFilter(String text) {
      this.text = text.toLowerCase();
   }

   @Override
   public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      String stack = item.getItemProperty("name").getValue().toString().toLowerCase();
      return stack.contains(text);
   }

   @Override
   public boolean appliesToProperty(Object propertyId) {
      return true;
   }
}