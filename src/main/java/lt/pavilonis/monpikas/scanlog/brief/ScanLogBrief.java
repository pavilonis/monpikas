package lt.pavilonis.monpikas.scanlog.brief;

import lt.pavilonis.monpikas.common.Identified;

import java.time.LocalDateTime;

public class ScanLogBrief extends Identified<Void> {

   private final LocalDateTime dateTime;
   private final String scanner;
   private final String cardCode;
   private final String name;
   private final String group;
   private final String role;
   private final String supervisor;

   public ScanLogBrief(LocalDateTime dateTime, String scannerName, String cardCode,
                       String name, String group, String role, String supervisor) {

      this.dateTime = dateTime;
      this.scanner = scannerName;
      this.cardCode = cardCode;
      this.name = name;
      this.group = group;
      this.role = role;
      this.supervisor = supervisor;
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

   public String getSupervisor() {
      return supervisor;
   }

   @Override
   public String toString() {
      return "ScanLogBrief{" +
            "dateTime=" + dateTime +
            ", scanner='" + scanner + '\'' +
            ", cardCode='" + cardCode + '\'' +
            ", name='" + name + '\'' +
            ", group='" + group + '\'' +
            ", role='" + role + '\'' +
            ", supervisor='" + supervisor + '\'' +
            '}';
   }
}
