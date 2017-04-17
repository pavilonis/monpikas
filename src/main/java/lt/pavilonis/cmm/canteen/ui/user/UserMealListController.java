package lt.pavilonis.cmm.canteen.ui.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class UserMealListController extends AbstractListController<UserMeal, String, UserMealFilter> {

   @Autowired
   private UserMealService userMealService;

   @Autowired
   private ImageService imageService;

   @Autowired
   private UserMealListFilterPanel filterPanel;

   @Override
   protected ListGrid<UserMeal> createGrid() {
      return new UserMealGrid();
   }

   @Override
   protected EntityRepository<UserMeal, String, UserMealFilter> getEntityRepository() {
      return userMealService;
   }

   @Override
   protected AbstractFormController<UserMeal, String> getFormController() {
      return new AbstractFormController<UserMeal, String>(UserMeal.class) {
         @Override
         protected EntityRepository<UserMeal, String, UserMealFilter> getEntityRepository() {
            return userMealService;
         }

         @Override
         protected FieldLayout<UserMeal> createFieldLayout() {
            return new UserMealFormView(imageService);
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

   @Override
   public String getViewName() {
      return "user-meals";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CHILD;
   }

   @Override
   public String getViewRole() {
      return "USER_MEALS";
   }

   @Override
   public String getViewGroupName() {
      return "canteen";
   }
}
