package lt.pavilonis.cmm.ui.security;

public class SecurityUserFilter {
   private final String username;
   private final String text;

   public SecurityUserFilter(String username, String text) {
      this.username = username;
      this.text = text;
   }

   public String getUsername() {
      return username;
   }

   public String getText() {
      return text;
   }
}
