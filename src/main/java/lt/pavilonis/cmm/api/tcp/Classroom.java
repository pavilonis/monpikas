package lt.pavilonis.cmm.api.tcp;

public class Classroom {

   private final Building building;
   private final int classNumber;

   public Classroom(Building building, int classNumber) {
      this.building = building;
      this.classNumber = classNumber;
   }

   public Building getBuilding() {
      return building;
   }

   public int getClassNumber() {
      return classNumber;
   }
}
