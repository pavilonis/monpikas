package lt.pavilonis.cmm.api.tcp;

import java.util.EnumSet;
import java.util.Optional;

public enum Building {

   SCHOOL('m'), BALLET('b'), THEATER('t'), FINE_ARTS('d'), DORMITORY('i');

   private final char code;

   Building(char code) {
      this.code = code;
   }

   public static Optional<Building> forCode(char searchCode) {

      char lowerCaseSearchCode = Character.toLowerCase(searchCode);

      return EnumSet.allOf(Building.class)
            .stream()
            .filter(building -> building.code == lowerCaseSearchCode)
            .findFirst();
   }

   public Character getCode() {
      return code;
   }
}
