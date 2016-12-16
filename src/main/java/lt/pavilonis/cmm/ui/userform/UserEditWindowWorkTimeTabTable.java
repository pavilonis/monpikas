package lt.pavilonis.cmm.ui.userform;

import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.WorkTimeRepresentation;
import org.vaadin.viritin.fields.MTable;

public class UserEditWindowWorkTimeTabTable extends MTable<WorkTimeRepresentation> {

   public UserEditWindowWorkTimeTabTable(UserRestRepository userRepository, String cardCode) {

      addBeans(userRepository.loadWorkTime(cardCode));
      setVisibleColumns("date", "startTime", "endTime", "hourDifference");
//      withProperties("date", "startTime", "endTime", "hourDifference");
      withColumnHeaders("Date", "Start Time", "End Time", "Hour Diff");
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setCacheRate(3);
      withFullWidth();
      setPageLength(12);
//      setHeight("550px");
      setSelectable(true);
      addStyleName("table-border-less");
      setNullSelectionAllowed(false);
//      addRowClickListener(click -> {
//         if (click.isDoubleClick()) {
//            editPopup.edit(click.getRow());
//         }
//      });
   }
}
