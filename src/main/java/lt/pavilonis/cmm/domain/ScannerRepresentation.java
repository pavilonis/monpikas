package lt.pavilonis.cmm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScannerRepresentation {
   private final long id;
   private final String name;

   public ScannerRepresentation(@JsonProperty("id") long id,
                                @JsonProperty("name") String name) {
      this.id = id;
      this.name = name;
   }

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}
