package lt.pavilonis.cmm.security.ui;

public final class SecurityUserFilter {
   private final Long id;
   private final String username;
   private final String text;

   public SecurityUserFilter(Long id, String username, String text) {
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
