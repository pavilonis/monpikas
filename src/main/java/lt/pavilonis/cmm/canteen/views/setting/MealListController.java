package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.user.MealTable;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.HiddenFilterPanel;
import lt.pavilonis.cmm.common.ListTable;
import org.springframework.beans.factory.annotation.Autowired;

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
         protected FormView<Meal> getFormView() {
            return new MealFormView();
         }

         @Override
         protected MessageSourceAdapter getMessageSource() {
            return MealListController.this.messages;
         }

         @Override
         protected String getFormCaption() {
            return getMessageSource().get(MealFormView.class, "caption");
         }
      };
   }

   @Override
   protected ListTable<Meal> getTable() {
      return new MealTable(messages);
   }

   @Override
   protected FilterPanel<MealFilter> getFilterPanel() {
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
