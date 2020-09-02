package lt.pavilonis.cmm.common.util;

import org.apache.commons.lang3.StringUtils;

public class QueryUtils {

   public static String likeArg(String value) {
      return StringUtils.isBlank(value)
            ? null
            : "%" + value + "%";
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
}
