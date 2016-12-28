package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.StringToBooleanConverter;

public class BooleanCellConverter extends StringToBooleanConverter {

   @Override
   protected String getTrueString() {
      return "✔";
   }

   @Override
   protected String getFalseString() {
      return null;
   }
}
