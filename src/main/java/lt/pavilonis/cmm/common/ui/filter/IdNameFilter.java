package lt.pavilonis.cmm.common.ui.filter;

public final class IdNameFilter {

   private final Long id;
   private final String name;

   private int offSet;
   private int limit;

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

   public IdNameFilter() {
      this(null, null);
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public int getOffSet() {
      return offSet;
   }

   public int getLimit() {
      return limit;
   }

   public IdNameFilter withOffset(int value) {
      this.offSet = value;
      return this;
   }

   public IdNameFilter withLimit(int value) {
      this.limit = value;
      return this;
   }
}
