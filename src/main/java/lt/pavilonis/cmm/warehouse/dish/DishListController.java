package lt.pavilonis.cmm.warehouse.dish;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.ui.filter.NameFilterPanel;
import lt.pavilonis.cmm.warehouse.dishGroup.DishGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class DishListController extends AbstractListController<Dish, Long, IdNameFilter> {

   @Autowired
   private DishRepository repository;

   @Autowired
   private DishGroupRepository dishGroupRepo;

   @Override
   protected ListGrid<Dish> createGrid() {
      return new ListGrid<Dish>(Dish.class) {
         @Override
         protected Map<String, ValueProvider<Dish, ?>> getCustomColumns() {
            return Collections.singletonMap("dishGroup", value -> value.getDishGroup().getName());
         }

         @Override
         protected List<String> getProperties() {
            return Arrays.asList("name", "dishGroup");
         }
      };
   }

   @Override
   protected AbstractFormController<Dish, Long> getFormController() {
      return new AbstractFormController<Dish, Long>(Dish.class) {
         @Override
         protected EntityRepository<Dish, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Dish> createFieldLayout() {
            return new DishForm(dishGroupRepo.load(new IdNameFilter()));
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<Dish, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Dish> getEntityClass() {
      return Dish.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CROSS_CUTLERY;
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
      return "dish";
   }
}
