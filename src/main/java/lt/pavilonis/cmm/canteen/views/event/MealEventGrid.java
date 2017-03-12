package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.shared.data.sort.SortDirection;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.converter.ADecimalFormat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class MealEventGrid extends ListGrid<MealEventLog> {

   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
   private static final DecimalFormat NUMBER_FORMAT = new ADecimalFormat();

   public MealEventGrid() {
      super(MealEventLog.class);
   }

   @Override
   protected List<String> getProperties(Class<MealEventLog> type) {
      return Arrays.asList("id", "cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
   }

   @Override
   protected void addCustomColumns() {
      addColumn(item -> DATE_FORMAT.format(item.getDate()))
            .setId("date");

      addColumn(item -> NUMBER_FORMAT.format(item.getPrice()))
            .setId("price");

      addColumn(item -> App.translate(MealType.class, item.getMealType().name()))
            .setId("mealType");

      addColumn(item -> App.translate(PupilType.class, item.getPupilType().name()))
            .setId("pupilType");
   }

   @Override
   protected void customize() {

      getColumn("cardCode").setWidth(180);
      getColumn("grade").setWidth(60);
      getColumn("birthDate").setWidth(130);

      sort("date", SortDirection.DESCENDING);
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Arrays.asList("id", "cardCode");
   }
}
