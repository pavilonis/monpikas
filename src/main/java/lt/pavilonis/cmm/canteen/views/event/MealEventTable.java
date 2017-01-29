package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.util.converter.StringToDateConverter;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MealEventTable extends ListTable<MealEventLog> {

   public MealEventTable() {
      super(MealEventLog.class);
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("id", "cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
   }

   @Override
   protected void customize(MessageSourceAdapter messageSource) {
      setConverter("date", new StringToDateConverter() {
         @Override
         public DateFormat getFormat(Locale locale) {
            return new SimpleDateFormat("yyyy-MM-dd  HH:mm");
         }
      });
      setConverter("price", new ModifiedStringToDoubleConverter());
      setConverter("mealType", new ToStringConverterAdapter<MealType>(MealType.class) {
         @Override
         protected String toPresentation(MealType model) {
            return messageSource.get(MealType.class, model.name());
         }
      });
      setConverter("pupilType", new ToStringConverterAdapter<PupilType>(PupilType.class) {
         @Override
         protected String toPresentation(PupilType model) {
            return messageSource.get(PupilType.class, model.name());
         }
      });

      setColumnWidth("cardCode", 180);
      setColumnWidth("grade", 60);
      setColumnWidth("birthDate", 130);

      setSortContainerPropertyId("date");
      setSortAscending(false);
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Arrays.asList("id", "cardCode");
   }
}
