package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@UIScope
@SpringComponent
public class UserMealListController extends AbstractListController<UserMeal, String> {

   @Autowired
   private UserMealTable table;

   @Autowired
   private UserMealService userMealService;

   @Override
   protected MTable<UserMeal> getTable() {
      return table;
   }

   @Override
   protected EntityRepository<UserMeal, String> getEntityRepository() {
      return userMealService;
   }
}
