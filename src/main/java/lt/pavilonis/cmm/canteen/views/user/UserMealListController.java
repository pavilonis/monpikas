package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.ListTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

@UIScope
@SpringComponent
public class UserMealListController extends AbstractListController<UserMeal, String, UserMealFilter> {

   @Autowired
   private UserMealService userMealService;

   @Autowired
   private UserMealListFilterPanel filterPanel;

   @Override
   protected ListTable<UserMeal> createTable() {
      return new UserMealTable();
   }

   @Autowired
   private UserMealFormView formView;

   @Override
   protected EntityRepository<UserMeal, String, UserMealFilter> getEntityRepository() {
      return userMealService;
   }

   @Override
   protected AbstractFormController<UserMeal, String> getFormController() {
      return new AbstractFormController<UserMeal, String>() {
         @Override
         protected EntityRepository<UserMeal, String, UserMealFilter> getEntityRepository() {
            return userMealService;
         }

         @Override
         protected FormView<UserMeal> createFormView() {
            return formView;
         }

         @Override
         protected MessageSourceAdapter getMessageSource() {
            return UserMealListController.this.messageSource;
         }

         @Override
         protected void beforeSave(UserMeal model) {
            List<Meal> mealTableValue = formView.getMealTableValue();
            model.getMealData().setMeals(new HashSet<>(mealTableValue));
         }
      };
   }

   @Override
   protected FilterPanel<UserMealFilter> createFilterPanel() {
      return filterPanel;
   }

   @Override
   protected Class<UserMeal> getEntityClass() {
      return UserMeal.class;
   }

   @Override
   protected Component getControlPanel() {
      return null;
   }
}
