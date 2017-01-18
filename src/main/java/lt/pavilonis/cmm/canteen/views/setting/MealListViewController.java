package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.user.form.MealTable;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class MealListViewController extends AbstractListController<Meal, Long> {

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private MealForm mealForm;

   @Override
   protected AbstractFormController<Meal, Long> getFormController() {
      return mealForm;
   }

   @Override
   protected MTable<Meal> getTable() {
      return new MealTable(messages);
   }

   @Override
   protected EntityRepository<Meal, Long> getEntityRepository() {
      return mealRepository;
   }

   @Override
   protected Class<Meal> getEntityClass() {
      return Meal.class;
   }
}
