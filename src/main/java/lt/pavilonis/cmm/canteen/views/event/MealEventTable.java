package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.canteen.views.converter.MealTypeCellConverter;
import lt.pavilonis.cmm.canteen.views.converter.PupilTypeCellConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
      reload();
      setSizeFull();
      setConverter("date", new StringToDateConverter() {
         @Override
         public DateFormat getFormat(Locale locale) {
            return new SimpleDateFormat("yyyy-MM-dd  HH:mm");
         }
      });
      setConverter("price", new StringToDoubleConverter() {
         @Override
         protected NumberFormat getFormat(Locale locale) {
            return new DecimalFormat("0.00");
         }
      });
      setConverter("mealType", new MealTypeCellConverter());
      setConverter("pupilType", new PupilTypeCellConverter());
      setVisibleColumns("id", "cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
      setColumnHeaders("Id", "Kodas", "Klasė", "Vardas", "Data", "Maitinimo tipas", "Mokinio tipas", "Kaina");
      setColumnWidth("cardCode", 100);
      setColumnWidth("grade", 60);
      setColumnAlignment("grade", CENTER);
      setColumnWidth("birthDate", 130);
      setColumnCollapsingAllowed(true);
      setColumnCollapsed("id", true);
      setColumnCollapsed("cardCode", true);
      setSelectable(true);
      setNullSelectionAllowed(false);
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
