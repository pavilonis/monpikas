package lt.pavilonis.cmm.ui.user.form;

import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.LocalTimeConverter;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;

import java.util.Arrays;
import java.util.List;

final class UserEditWindowPresenceTimeTabTable extends ListTable<PresenceTimeRepresentation> {

   private static final LocalTimeConverter TIME_CONVERTER = new LocalTimeConverter();

   UserEditWindowPresenceTimeTabTable(UserRestRepository userRepository, String cardCode) {
      super(PresenceTimeRepresentation.class);

      addBeans(userRepository.loadPresenceTime(cardCode));

      setHeight(430, Unit.PIXELS);
      addStyleName("table-border-less");

      setConverter("startTime", TIME_CONVERTER);
      setConverter("endTime", TIME_CONVERTER);
      setCellStyleGenerator(new PresenceTimeCellStyleGenerator());
   }

   @Override
   protected List<String> getProperties(Class<PresenceTimeRepresentation> type) {
      return Arrays.asList("date", "startTime", "endTime", "hourDifference");
   }
}
