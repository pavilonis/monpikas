package lt.pavilonis.cmm.ui.user.form;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.converter.LocalTimeConverter;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import org.vaadin.viritin.fields.MTable;

public class UserEditWindowPresenceTimeTabTable extends MTable<PresenceTimeRepresentation> {

   private static final LocalTimeConverter TIME_CONVERTER = new LocalTimeConverter();

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

      setConverter("startTime", TIME_CONVERTER);
      setConverter("endTime", TIME_CONVERTER);

      setCellStyleGenerator(new PresenceTimeCellStyleGenerator());
   }
}
