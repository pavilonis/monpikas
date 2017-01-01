package lt.pavilonis.cmm.canteen.views.converters;

import com.vaadin.data.util.converter.StringToDoubleConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ModifiedStringToDoubleConverter extends StringToDoubleConverter {
   @Override
   protected NumberFormat getFormat(Locale locale) {
      return new DecimalFormat("0.00");
   }
}
