package lt.pavilonis.cmm.common.util;

import org.apache.commons.lang3.StringUtils;

public class QueryUtils {
   public static String likeArg(String text) {
      return StringUtils.isBlank(text) ? null : "%" + text + "%";
   }
}
