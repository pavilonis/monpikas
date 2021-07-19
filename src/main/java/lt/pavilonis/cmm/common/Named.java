package lt.pavilonis.cmm.common;

import javax.validation.constraints.NotBlank;

public class Named<ID> extends Identified<ID> {

   public Named(ID id, String name) {
      setId(id);
      this.name = name;
   }

   public Named() {
   }

   @NotBlank
   private String name;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
