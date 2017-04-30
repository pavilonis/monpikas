package lt.pavilonis.cmm.canteen.ui.user;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.converter.CollectionValueProviderAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserEatingGrid extends ListGrid<UserEating> {

   public UserEatingGrid() {
      super(UserEating.class);
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("user.cardCode", "user.name", "user.birthDate",
            "user.group", "eatingData.eatings", "eatingData.comment");
   }

   @Override
   protected Map<String, ValueProvider<UserEating, ?>> getCustomColumns() {
      return ImmutableMap.<String, ValueProvider<UserEating, ?>>builder()

            .put("user.cardCode", item -> item.getUser().getCardCode())
            .put("user.name", item -> item.getUser().getName())
            .put("user.birthDate", item -> item.getUser().getBirthDate())
            .put("user.group", item -> item.getUser().getGroup())
            .put("eatingData.eatings", new CollectionValueProviderAdapter<>(item -> item.getEatingData().getEatings()))
            .put("eatingData.comment", item -> item.getEatingData().getComment())
            .build();
   }

   @Override
   protected void customize() {
      getColumn("user.group").setWidth(90);
      getColumn("user.group").setWidth(90);
      getColumn("user.birthDate").setWidth(130);
      getColumn("user.cardCode").setWidth(180);
      getColumn("user.name").setWidth(300);
      sort("user.name");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("user.cardCode");
   }
}
