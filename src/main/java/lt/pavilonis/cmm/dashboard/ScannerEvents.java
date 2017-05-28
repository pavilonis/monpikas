package lt.pavilonis.cmm.dashboard;

import lt.pavilonis.cmm.common.Identified;

public final class ScannerEvents extends Identified<Void> {

   private final String scannerName;
   private final int scansToday;
   private final int scansTotal;

   public ScannerEvents(String scannerName, int scansToday, int scansTotal) {
      this.scannerName = scannerName;
      this.scansToday = scansToday;
      this.scansTotal = scansTotal;
   }

   public String getScannerName() {
      return scannerName;
   }

   public int getScansToday() {
      return scansToday;
   }

   public int getScansTotal() {
      return scansTotal;
   }
}
