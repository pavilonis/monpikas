package lt.pavilonis.cmm.warehouse.supplier;

public final class Supplier {

   private Long id;
   private String code;
   private String name;

   public Supplier(String code, String name) {
      this.code = code;
      this.name = name;
   }

   public String getCode() {
      return code;
   }

   public String getName() {
      return name;
   }
}
