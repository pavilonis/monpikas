package lt.pavilonis.monpikas.common.util;

import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class QueryUtils {

   public static String likeArg(String value) {
      return StringUtils.hasText(value)
            ? "%" + value + "%"
            : null;
   }

   public static long argOffset(Integer value) {
      return value == null
            ? 0
            : value;
   }

   /**
    * Integer is used because this type is used in
    * com.vaadin.data.provider.Query interface limit/offset methods
    */
   public static long argLimit(Integer value) {
      return value == null
            ? Integer.MAX_VALUE
            : value;
   }

   public static LocalDate getLocalDate(ResultSet rs, String property) throws SQLException {
      Date date = rs.getDate(property);
      return date == null ? null : date.toLocalDate();
   }
}
