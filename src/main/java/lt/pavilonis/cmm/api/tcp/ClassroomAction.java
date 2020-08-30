package lt.pavilonis.cmm.api.tcp;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Optional;

public enum ClassroomAction {

   FREE("Endofprivacy", false), OCCUPY("Privacystarted", true);

   private final boolean booleanValue;
   private final String code;

   ClassroomAction(String code, boolean booleanValue) {
      this.booleanValue = booleanValue;
      this.code = code;
   }

   public boolean getBooleanValue() {
      return booleanValue;
   }

   public static Optional<ClassroomAction> forCode(String code) {
      return EnumSet.allOf(ClassroomAction.class)
            .stream()
            .filter(action -> StringUtils.equalsIgnoreCase(action.code, code))
            .findFirst();
   }
}
