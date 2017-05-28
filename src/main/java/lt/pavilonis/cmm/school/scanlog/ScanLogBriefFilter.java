package lt.pavilonis.cmm.school.scanlog;

import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;

import java.time.LocalDate;

public final class ScanLogBriefFilter extends IdPeriodFilter {

   private final String text;
   private final Scanner scanner;
   private final String role;

   public ScanLogBriefFilter(LocalDate periodStart, LocalDate periodEnd,
                             String text, Scanner scanner, String role) {

      super(periodStart, periodEnd);
      this.text = text;
      this.scanner = scanner;
      this.role = role;
   }

   public String getText() {
      return text;
   }

   public Scanner getScanner() {
      return scanner;
   }

   public String getRole() {
      return role;
   }
}
