package lt.pavilonis.cmm.classroom;

import lt.pavilonis.cmm.common.Identifiable;

import java.time.LocalDateTime;

public class ClassroomOccupancy implements Identifiable<Void> {

   private final LocalDateTime dateTime;
   private final int classroomNumber;
   private final boolean occupied;

   public ClassroomOccupancy(LocalDateTime dateTime, boolean occupied, int classroomNumber) {
      this.dateTime = dateTime;
      this.classroomNumber = classroomNumber;
      this.occupied = occupied;
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
}
