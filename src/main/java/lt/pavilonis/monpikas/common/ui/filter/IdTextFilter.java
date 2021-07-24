package lt.pavilonis.monpikas.common.ui.filter;

public class IdTextFilter {

   private final Long id;
   private final String text;

   private int offSet;
   private int limit;

   public IdTextFilter(Long id, String text) {
      this.id = id;
      this.text = text;
   }

   public IdTextFilter(String text) {
      this(null, text);
   }

   public IdTextFilter(Long id) {
      this(id, null);
   }

   public IdTextFilter() {
      this(null, null);
   }

   public Long getId() {
      return id;
   }

   public String getText() {
      return text;
   }

   public int getOffSet() {
      return offSet;
   }

   public int getLimit() {
      return limit;
   }

   public IdTextFilter withOffset(int value) {
      this.offSet = value;
      return this;
   }

   public IdTextFilter withLimit(int value) {
      this.limit = value;
      return this;
   }

   public static IdTextFilter empty() {
      return new IdTextFilter();
   }
}
