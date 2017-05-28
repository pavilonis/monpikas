package lt.pavilonis.cmm.warehouse.supplier;

import lt.pavilonis.cmm.common.Named;

public final class Supplier extends Named<Long> {

   private String code;

   public Supplier() {
   }

   public Supplier(Long id, String code, String name) {
      setId(id);
      this.code = code;
      setName(name);
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }
}
