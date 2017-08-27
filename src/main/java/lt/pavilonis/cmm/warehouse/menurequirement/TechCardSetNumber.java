package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;

public class TechCardSetNumber extends Identified<Void> {

   private TechCardSet techCardSet;
   private int number = 1;

   public TechCardSetNumber(TechCardSet techCardSet, int number) {
      this.techCardSet = techCardSet;
      this.number = number;
   }

   public TechCardSet getTechCardSet() {
      return techCardSet;
   }

   public void setTechCardSet(TechCardSet techCardSet) {
      this.techCardSet = techCardSet;
   }

   public int getNumber() {
      return number;
   }

   public void setNumber(int number) {
      this.number = number;
   }
}
