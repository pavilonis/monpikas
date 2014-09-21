package lt.pavilonis.monpikas.server.domain;

import java.time.LocalDateTime;

public class ClientPupilDto {
   private String name;
   private boolean dinnerPermission;
   private LocalDateTime lastDinner;

   public ClientPupilDto(String name, boolean dinnerPermission, LocalDateTime lastDinner) {
      this.name = name;
      this.dinnerPermission = dinnerPermission;
      this.lastDinner = lastDinner;
   }

   @Override
   public String toString() {
      return "ClientPupilDto: "+name+ ", DinnerPerm: "+dinnerPermission+", LastDinner: "+lastDinner;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isDinnerPermission() {
      return dinnerPermission;
   }

   public void setDinnerPermission(boolean dinnerPermission) {
      this.dinnerPermission = dinnerPermission;
   }

   public LocalDateTime getLastDinner() {
      return lastDinner;
   }

   public void setLastDinner(LocalDateTime lastDinner) {
      this.lastDinner = lastDinner;
   }
}
