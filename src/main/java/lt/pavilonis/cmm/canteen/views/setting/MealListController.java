package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.user.MealGrid;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.HiddenFilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@SpringComponent
@UIScope
public class MealListController extends AbstractListController<Meal, Long, MealFilter> {

   @Autowired
   private MealRepository mealRepository;

   @Override
   protected AbstractFormController<Meal, Long> getFormController() {
      return new AbstractFormController<Meal, Long>() {

         @Override
         protected EntityRepository<Meal, Long, ?> getEntityRepository() {
            return mealRepository;
         }

         @Override
         protected FormView<Meal> createFormView() {
            return new MealFormView();
         }

         @Override
         protected MessageSourceAdapter getMessageSource() {
            return MealListController.this.messageSource;
         }
      };
   }

   @Override
   protected ListGrid<Meal> createGrid() {
      return new MealGrid(Collections.emptyList());
   }

   @Override
   protected FilterPanel<MealFilter> createFilterPanel() {
      return new HiddenFilterPanel<>();
   }

   @Override
   protected EntityRepository<Meal, Long, MealFilter> getEntityRepository() {
      return mealRepository;
   }

   @Override
   protected Class<Meal> getEntityClass() {
      return Meal.class;
   }
}
