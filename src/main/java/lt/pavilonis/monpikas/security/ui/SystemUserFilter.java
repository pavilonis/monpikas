package lt.pavilonis.monpikas.security.ui;

public final class SystemUserFilter {

   private final Long id;
   private final String username;
   private final String text;

   public SystemUserFilter(Long id, String username, String text) {
      this.id = id;
      this.username = username;
      this.text = text;
   }

   public Long getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public String getText() {
      return text;
   }
}
