package lt.pavilonis.monpikas.scanner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.pavilonis.monpikas.common.Named;

public class Scanner extends Named<Long> {

   public Scanner() {/**/}

   public Scanner(@JsonProperty("id") long id, @JsonProperty("name") String name) {
      super(id, name);
   }
}
