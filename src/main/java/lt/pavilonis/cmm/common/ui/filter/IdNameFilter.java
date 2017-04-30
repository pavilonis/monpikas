package lt.pavilonis.cmm.common.ui.filter;

public final class IdNameFilter {

   private final Long id;
   private final String name;

   public IdNameFilter(Long id, String name) {
      this.id = id;
      this.name = name;
   }

   public IdNameFilter(String text) {
      this(null, text);
   }

   public IdNameFilter(Long id) {
      this(id, null);
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}
