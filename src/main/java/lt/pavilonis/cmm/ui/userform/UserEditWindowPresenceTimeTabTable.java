package lt.pavilonis.cmm.ui.userform;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.PresenceTimeRepresentation;
import org.vaadin.viritin.fields.MTable;

public class UserEditWindowPresenceTimeTabTable extends MTable<PresenceTimeRepresentation> {

   public UserEditWindowPresenceTimeTabTable(UserRestRepository userRepository,
                                             String cardCode,
                                             MessageSourceAdapter messages) {

      withProperties("date", "startTime", "endTime", "hourDifference");
      addBeans(userRepository.loadPresenceTime(cardCode));
      setColumnHeaders(
            messages.get(this, "date"),
            messages.get(this, "startTime"),
            messages.get(this, "endTime"),
            messages.get(this, "hourDifference")
      );
//      setVisibleColumns("date", "startTime", "endTime", "hourDifference");
//      withColumnHeaders("Date", "Start Time", "End Time", "Hour Diff");
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
