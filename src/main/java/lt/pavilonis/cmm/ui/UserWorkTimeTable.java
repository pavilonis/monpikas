package lt.pavilonis.cmm.ui;

import lt.pavilonis.cmm.UserRestRepository;
import org.vaadin.viritin.fields.MTable;

public class UserWorkTimeTable extends MTable<WorkTimeRepresentation> {

   public UserWorkTimeTable(UserRestRepository userRepository, String cardCode) {

      addBeans(userRepository.loadWorkTime(cardCode));
//      withProperties("date", "startTime", "endTime", "hourDifference");
      withColumnHeaders("Date", "Start Time", "End Time", "Hour Diff");
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setCacheRate(3);
      withFullWidth();
      setHeight("550px");
      setSelectable(true);
//      addRowClickListener(click -> {
//         if (click.isDoubleClick()) {
//            editPopup.edit(click.getRow());
//         }
//      });
   }
}
