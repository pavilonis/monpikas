package lt.pavilonis.cmm.canteen.views.event;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
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
import java.util.Map;

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
   protected Map<String, ValueProvider<MealEventLog, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "date", item -> DATE_FORMAT.format(item.getDate()),
            "price", item -> NUMBER_FORMAT.format(item.getPrice()),
            "mealType", item -> App.translate(MealType.class, item.getMealType().name()),
            "pupilType", item -> App.translate(PupilType.class, item.getPupilType().name())
      );
   }

   @Override
   protected void customize() {
      getColumn("cardCode").setWidth(180);
      getColumn("grade").setWidth(80);
      sort("date", SortDirection.DESCENDING);
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Arrays.asList("id", "cardCode");
   }
}
