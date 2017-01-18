package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.vaadin.ui.Table.Align.CENTER;

@UIScope
@SpringComponent
public class MealEventTable extends MTable<MealEventLog> {

   private final MealService mealService;

   @Autowired
   public MealEventTable(MealService mealService, MessageSourceAdapter messages) {
      this.mealService = mealService;

      withProperties("cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
      withColumnHeaders("Kodas", "Klasė", "Vardas", "Data", "Maitinimo tipas", "Mokinio tipas", "Kaina");

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
      setColumnAlignment("grade", CENTER);
      setColumnWidth("birthDate", 130);
      setColumnCollapsingAllowed(true);
      setColumnCollapsed("cardCode", true);
      setSelectable(true);
      setNullSelectionAllowed(false);
      setSortContainerPropertyId("date");
      setSortAscending(false);
      setCacheRate(5);
   }

   void updateContainer(String searchString, boolean hadMealTodayOnly) {
      removeAllItems();
      addBeans(mealService.getDinnerEventList());
   }

   void reload() {
      updateContainer(null, false);
   }
}
