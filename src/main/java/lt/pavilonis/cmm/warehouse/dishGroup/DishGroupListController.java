package lt.pavilonis.cmm.warehouse.dishGroup;

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
public class DishGroupListController extends AbstractListController<DishGroup, Long, IdNameFilter> {

   @Autowired
   private DishGroupRepository repository;

   @Override
   protected ListGrid<DishGroup> createGrid() {
      return new ListGrid<>(DishGroup.class);
   }

   @Override
   protected AbstractFormController<DishGroup, Long> getFormController() {
      return new AbstractFormController<DishGroup, Long>(DishGroup.class) {
         @Override
         protected EntityRepository<DishGroup, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<DishGroup> createFieldLayout() {
            return new DishGroupForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<DishGroup, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<DishGroup> getEntityClass() {
      return DishGroup.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.SPOON;
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
      return "dish-group";
   }
}
