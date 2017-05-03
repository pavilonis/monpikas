package lt.pavilonis.cmm.warehouse.meal;

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
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class MealListController extends AbstractListController<Meal, Long, IdNameFilter> {

   @Autowired
   private MealRepository repository;

   @Override
   protected ListGrid<Meal> createGrid() {
      return new ListGrid<Meal>(Meal.class) {

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("name", "unitWeight", "measureUnit", "productGroup");
         }
      };
   }

   @Override
   protected AbstractFormController<Meal, Long> getFormController() {
      return new AbstractFormController<Meal, Long>(Meal.class) {
         @Override
         protected EntityRepository<Meal, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<Meal> createFieldLayout() {
//            return new MealForm(productGroupRepo.load(new IdNameFilter()));
            return new MealForm();
         }
      };
   }

   @Override
   protected FilterPanel<IdNameFilter> createFilterPanel() {
      return new NameFilterPanel();
   }

   @Override
   protected EntityRepository<Meal, Long, IdNameFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Meal> getEntityClass() {
      return Meal.class;
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.PACKAGE;
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
      return "product";
   }
}
