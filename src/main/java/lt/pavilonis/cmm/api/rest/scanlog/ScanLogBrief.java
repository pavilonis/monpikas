package lt.pavilonis.cmm.api.rest.scanlog;

import lt.pavilonis.cmm.common.Identified;

import java.time.LocalDateTime;

public class ScanLogBrief extends Identified<Void> {

   private final LocalDateTime dateTime;
   private final String scanner;
   private final String cardCode;
   private final String name;
   private final String group;
   private final String role;
   private final String location;

   public ScanLogBrief(LocalDateTime dateTime, String scannerName, String cardCode,
                       String name, String group, String role, String location) {

      this.dateTime = dateTime;
      this.scanner = scannerName;
      this.cardCode = cardCode;
      this.name = name;
      this.group = group;
      this.role = role;
      this.location = location;
   }

   public LocalDateTime getDateTime() {
      return dateTime;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getName() {
      return name;
   }

   public String getGroup() {
      return group;
   }

   public String getRole() {
      return role;
   }

   public String getScanner() {
      return scanner;
   }

   public String getLocation() {
      return location;
   }
}
