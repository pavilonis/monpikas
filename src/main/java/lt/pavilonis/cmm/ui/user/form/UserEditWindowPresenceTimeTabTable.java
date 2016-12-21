package lt.pavilonis.cmm.ui.user.form;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.converter.LocalTimeConverter;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;
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

      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setCacheRate(3);
      withFullWidth();
      setHeight("481px");
      setSelectable(true);
      addStyleName("table-border-less");
      setNullSelectionAllowed(false);

      LocalTimeConverter timeConverter = new LocalTimeConverter();
      setConverter("startTime", timeConverter);
      setConverter("endTime", timeConverter);
   }
}
