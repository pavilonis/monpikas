package lt.pavilonis.cmm.common;

import org.hibernate.validator.constraints.NotBlank;

public class Named<ID> extends Identified<ID> {

   @NotBlank
   private String name;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
