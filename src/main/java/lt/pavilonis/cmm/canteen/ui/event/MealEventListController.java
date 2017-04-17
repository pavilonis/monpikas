package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UIScope
@SpringComponent
public class MealEventListController extends AbstractListController<MealEventLog, Long, MealEventFilter> {

   @Autowired
   private MealEventLogRepository eventLogs;

   @Autowired
   private MealEventFilterPanel filterPanel;

   @Autowired
   private MealEventFormController mealEventFormController;

   @Override
   protected void addGridClickListener(ListGrid<MealEventLog> table) {/*do nothing*/}

   @Override
   protected ListGrid<MealEventLog> createGrid() {
      return new MealEventGrid();
   }

   @Override
   protected EntityRepository<MealEventLog, Long, MealEventFilter> getEntityRepository() {
      return eventLogs;
   }

   @Override
   protected FilterPanel<MealEventFilter> createFilterPanel() {
      return filterPanel;
   }

   @Override
   protected Class<MealEventLog> getEntityClass() {
      return MealEventLog.class;
   }

   @Override
   protected AbstractFormController<MealEventLog, Long> getFormController() {
      return mealEventFormController;
   }

   @Override
   protected MealEventLog createNewInstance() {
      MealEventLog entity = new MealEventLog();
      entity.setDate(new Date());
      entity.setMealType(MealType.DINNER);
      return entity;
   }

   @Override
   public String getViewName() {
      return "meal-events";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CUTLERY;
   }

   @Override
   public String getViewRole() {
      return "MEAL_EVENTS";
   }

   @Override
   public String getViewGroupName() {
      return "canteen";
   }
}
