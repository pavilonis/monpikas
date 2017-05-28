package lt.pavilonis.cmm.canteen.ui.event;


import com.vaadin.data.Binder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.canteen.service.UserEatingService;
import lt.pavilonis.cmm.canteen.ui.user.UserEatingFilter;
import lt.pavilonis.cmm.canteen.ui.user.UserEatingGrid;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

final class EatingEventFormView extends FieldLayout<EatingEvent> {

   private final UserEatingGrid grid;
   private final UserEatingService service;
   private final TextField textFilter = new ATextField(getClass(), "name");
   private final EnumComboBox<EatingType> eatingType = new EnumComboBox<>(EatingType.class)
         .withRequired(true);
   private final ADateField dateField = new ADateField(getClass(), "date")
         .withValue(LocalDate.now())
         .withRequired();

   EatingEventFormView(UserEatingService service) {
      this.service = service;
      this.grid = new UserEatingGrid();
      grid.setWidth(100, Unit.PERCENTAGE);
      grid.setHeight(340, Unit.PIXELS);

      List<String> columns = Arrays.asList("user.name", "user.group");
      grid.getColumns().stream()
            .filter(col -> !columns.contains(col.getId()))
            .forEach(col -> col.setHidden(true));

      addComponents(new HorizontalLayout(dateField, eatingType), textFilter, grid);

      textFilter.addValueChangeListener(change -> updateTable(change.getValue(), eatingType.getValue()));
      eatingType.addValueChangeListener(change -> updateTable(textFilter.getValue(), eatingType.getValue()));
      updateTable(null, eatingType.getValue());
   }

   private void updateTable(String text, EatingType value) {
      UserEatingFilter filter = new UserEatingFilter(value, text, true);
      List<UserEating> beans = service.load(filter);

      grid.deselectAll();
      grid.setItems(beans);
   }

   @Override
   public void manualBinding(Binder<EatingEvent> binding) {
      binding.bind(
            dateField,
            event -> LocalDateTime.ofInstant(
                  event.getDate().toInstant(), ZoneId.systemDefault()
            ).toLocalDate(),

            (event, localDate) -> {
               ZonedDateTime zonedDateTime = localDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault());
               event.setDate(Date.from(zonedDateTime.toInstant()));

               //TODO use oneToMany field to get value like from other fields
               setSelectedUserData(event);
            });
   }

   private void setSelectedUserData(EatingEvent model) {
      Set<UserEating> value = grid.getSelectedItems();

      if (CollectionUtils.isNotEmpty(value)
            && value.size() == 1
            && eatingType.getValue() != null) {

         UserEating selectedUserEating = value.iterator().next();

         Eating eating = selectedUserEating.getEatingData()
               .getEatings()
               .stream()
               .filter(portion -> portion.getType() == eatingType.getValue())
               .findFirst()
               .orElseThrow(() -> new RuntimeException("Could not find required eating type. Should not happen"));

         model.setDate(correcEventTime(model.getDate(), eating));
         model.setName(selectedUserEating.getUser().getName());
         model.setCardCode(selectedUserEating.getUser().getCardCode());
         model.setGrade(selectedUserEating.getUser().getGroup());
         model.setPupilType(selectedUserEating.getEatingData().getType());
         model.setPrice(eating.getPrice());
      }
   }

   private Date correcEventTime(Date date, Eating eating) {
      return org.joda.time.LocalDateTime.fromDateFields(date)
            .withTime(0, 0, 0, 0)
            .plusSeconds(eating.getStartTime().toSecondOfDay())
            .toDate();
   }
}
