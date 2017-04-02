package lt.pavilonis.cmm.ui.security;

public final class SecurityUserFilter {
   private final Long id;
   private final String username;
   private final String text;

   public SecurityUserFilter() {
      this(null, null, null);
   }

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
