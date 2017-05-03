package lt.pavilonis.cmm.warehouse.mealtype;

import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MealTypeListController extends AbstractListController<MealType, Long, IdNameFilter> {

   @Autowired
   private MealTypeRepository repository;

   @Override
   protected ListGrid<MealType> createGrid() {
      return new ListGrid<>(MealType.class);
   }

   @Override
   protected AbstractFormController<MealType, Long> getFormController() {
      return new AbstractFormController<MealType, Long>(MealType.class) {
         @Override
         protected EntityRepository<MealType, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<MealType> createFieldLayout() {
            return new MealTypeForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<MealType, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<MealType> getEntityClass() {
      return MealType.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CLOCK;
   }

   @Override
   public String getViewRole() {
      return "WAREHOUSE";
   }

   @Override
   public String getViewGroupName() {
      return "warehouse";
   }

   @Override
   public String getViewName() {
      return "meal-type";
   }
}
