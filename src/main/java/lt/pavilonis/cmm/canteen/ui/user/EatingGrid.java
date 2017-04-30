package lt.pavilonis.cmm.canteen.ui.user;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.common.ListGrid;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class EatingGrid extends ListGrid<Eating> {

   private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");

   public EatingGrid(List<Eating> eatings) {
      super(Eating.class);
      setItems(eatings);
   }

   @Override
   protected Map<String, ValueProvider<Eating, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "type", eating -> messages.get(eating.getType().getClass(), eating.getType().name()),
            "price", eating -> NUMBER_FORMAT.format(eating.getPrice())
      );
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("id", "name", "type", "startTime", "endTime", "price");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("id");
   }
}
