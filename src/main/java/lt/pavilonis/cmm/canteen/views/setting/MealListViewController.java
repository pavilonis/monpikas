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

import java.util.Optional;

@SpringComponent
@UIScope
public class MealListViewController extends AbstractListController<Meal, Long> {

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private MealForm mealForm;

   @Override
   protected Optional<AbstractFormController<Meal, Long>> getFormController() {
      return Optional.of(mealForm);
   }

   @Override
   protected MTable<Meal> getTable() {
      return new MealTable();
   }

   @Override
   protected EntityRepository<Meal, Long> getEntityRepository() {
      return mealRepository;
   }
}
