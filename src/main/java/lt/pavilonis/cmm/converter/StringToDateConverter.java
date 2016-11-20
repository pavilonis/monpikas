package lt.pavilonis.cmm.converter;

import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.util.Objects.isNull;

public class StringToDateConverter implements Converter<Date, String> {

   private static final Logger LOG = LoggerFactory.getLogger(StringToDateConverter.class);
   private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

   @Override
   public String convertToModel(Date value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return isNull(value)
            ? null
            : DATE_FORMAT.format(value);
   }

   @Override
   public Date convertToPresentation(String value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
      if (StringUtils.isBlank(value)) {
         return null;
      }

      try {
         return DATE_FORMAT.parse(value);
      } catch (ParseException e) {
         LOG.error("Could not extract Date from string [string={}]", value);
         return null;
      }
   }

   @Override
   public Class<String> getModelType() {
      return String.class;
   }

   @Override
   public Class<Date> getPresentationType() {
      return Date.class;
   }
}
