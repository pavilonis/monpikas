package lt.pavilonis.cmm.api.rest.classroom;

import lt.pavilonis.cmm.common.Identified;

import java.time.LocalDateTime;

public class ClassroomOccupancy extends Identified<Void> {

   private final LocalDateTime dateTime;
   private final int classroomNumber;
   private final String building;
   private final boolean occupied;

   public ClassroomOccupancy(LocalDateTime dateTime, boolean occupied,
                             int classroomNumber, String building) {
      this.dateTime = dateTime;
      this.classroomNumber = classroomNumber;
      this.occupied = occupied;
      this.building = building;
   }

   public boolean isOccupied() {
      return occupied;
   }

   public int getClassroomNumber() {
      return classroomNumber;
   }

   public LocalDateTime getDateTime() {
      return dateTime;
   }

   public String getBuilding() {
      return building;
   }
}
