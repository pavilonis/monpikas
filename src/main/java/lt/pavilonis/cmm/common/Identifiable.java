package lt.pavilonis.cmm.common;

import org.apache.commons.lang3.NotImplementedException;

public interface Identifiable<ID> {
   default ID getId() {
      throw new NotImplementedException("Implement if needed");
   }
}
