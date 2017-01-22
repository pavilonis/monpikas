package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.vaadin.ui.Table.Align.CENTER;

@UIScope
@SpringComponent
public class MealEventTable extends ListTable<MealEventLog> {

   @Autowired
   public MealEventTable(MessageSourceAdapter messages) {

      withProperties("id", "cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
      withColumnHeaders("ID", "Kodas", "Klasė", "Vardas", "Data", "Maitinimo tipas", "Mokinio tipas", "Kaina");

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
            return messages.get(MealType.class, model.name());
         }
      });
      setConverter("pupilType", new ToStringConverterAdapter<PupilType>(PupilType.class) {
         @Override
         protected String toPresentation(PupilType model) {
            return messages.get(PupilType.class, model.name());
         }
      });

      setColumnWidth("cardCode", 180);
      setColumnWidth("grade", 60);
      setColumnWidth("birthDate", 130);

      setColumnAlignment("grade", CENTER);

      setSortContainerPropertyId("date");
      setSortAscending(false);
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Arrays.asList("id", "cardCode");
   }
}
