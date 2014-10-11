package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.StringToBooleanConverter;

public class StringToBooleanCellConverter extends StringToBooleanConverter {
   @Override
   protected String getTrueString() {
      return "✔";
   }

   @Override
   protected String getFalseString() {
      return "";
   }
}
