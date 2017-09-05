package lt.pavilonis.cmm.canteen.ui.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.canteen.service.UserEatingService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.service.ImageService;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UIScope
@SpringComponent
public class UserEatingListController extends AbstractListController<UserEating, String, UserEatingFilter> {

   @Autowired
   private UserEatingService repository;

   @Autowired
   private ImageService imageService;

   @Override
   protected ListGrid<UserEating> createGrid() {
      return new UserEatingGrid();
   }

   @Override
   protected EntityRepository<UserEating, String, UserEatingFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected AbstractFormController<UserEating, String> getFormController() {
      return new AbstractFormController<UserEating, String>(UserEating.class) {
         @Override
         protected EntityRepository<UserEating, String, UserEatingFilter> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<UserEating> createFieldLayout(UserEating model) {
            return new UserEatingFieldLayout(imageService);
         }
      };
   }

   @Override
   protected FilterPanel<UserEatingFilter> createFilterPanel() {
      return new UserEatingListFilterPanel();
   }

   @Override
   protected Class<UserEating> getEntityClass() {
      return UserEating.class;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
   }

   @Override
   public String getViewName() {
      return "user-eatings";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.CHILD;
   }

   @Override
   public String getViewRole() {
      return "USER_EATINGS";
   }

   @Override
   public String getViewGroupName() {
      return "canteen";
   }
}
