package lt.pavilonis.cmm.classroom;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class ClassroomListGrid extends ListGrid<ClassroomOccupancy> {

   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm");

   public ClassroomListGrid() {
      super(ClassroomOccupancy.class);
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("dateTime", "classroomNumber", "occupied", "dateTime",
            "user.name", "user.group", "user.role", "keyAction");
   }

   @Override
   protected Map<String, ValueProvider<ClassroomOccupancy, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "dateTime", classroom -> DATE_TIME_FORMAT.format(classroom.getDateTime()),
            "occupied", classroom -> App.translate(classroom, String.valueOf(classroom.isOccupied()))
      );
   }
}
