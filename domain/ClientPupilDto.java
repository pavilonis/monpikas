package lt.pavilonis.monpikas.server.domain;

public class ClientPupilDto {

   private String name;
   private boolean dinnerPermitted;
   private Boolean hadDinnerToday;

   public ClientPupilDto(String name, boolean dinnerPermitted, Boolean hadDinnerToday) {
      this.name = name;
      this.dinnerPermitted = dinnerPermitted;
      this.hadDinnerToday = hadDinnerToday;
   }

   @Override
   public String toString() {
      return "ClientPupilDto: " + name + ", DinnerPerm: " + dinnerPermitted + ", Had Dinner Today: " + hadDinnerToday;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isDinnerPermitted() {
      return dinnerPermitted;
   }

   public void setDinnerPermitted(boolean dinnerPermitted) {
      this.dinnerPermitted = dinnerPermitted;
   }

   public boolean isHadDinnerToday() {
      return hadDinnerToday;
   }

   public void setHadDinnerToday(boolean hadDinnerToday) {
      this.hadDinnerToday = hadDinnerToday;
   }
}
