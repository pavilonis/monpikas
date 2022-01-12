package lt.pavilonis.monpikas.scanner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lt.pavilonis.monpikas.common.Named;

@NoArgsConstructor
@SuperBuilder
public class Scanner extends Named<Long> {

   public Scanner(@JsonProperty("id") long id,
                  @JsonProperty("name") String name) {
      super(id, name);
   }
}
