package lt.pavilonis.cmm.security.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.ControlPanel;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.field.AButton;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.security.service.SecurityUserPasswordChangeService;
import lt.pavilonis.cmm.security.service.SecurityUserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringComponent
@UIScope
public class SecurityUserListController extends AbstractListController<SecurityUser, Long, SecurityUserFilter> {

   @Autowired
   private SecurityUserRepository repository;

   @Autowired
   private SecurityUserPasswordChangeService passwordChangeService;

   @Override
   protected ListGrid<SecurityUser> createGrid() {

      return new ListGrid<SecurityUser>(SecurityUser.class) {

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("username", "name", "email", "enabled", "authorities");
         }
      };
   }

   @Override
   protected FilterPanel<SecurityUserFilter> createFilterPanel() {
      return new SecurityUserFilterPanel();
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      ControlPanel controls = new ControlPanel(click -> actionCreate(), click -> actionDelete());
      controls.addComponent(
            new AButton(getClass(), "passwordReset")
                  .withClickListener(click -> {
                     @SuppressWarnings("unchecked")
                     ListGrid<SecurityUser> grid = (ListGrid<SecurityUser>) mainArea;
                     Set<SecurityUser> selection = grid.getSelectedItems();

                     if (CollectionUtils.isEmpty(selection)
                           || selection.size() > 1) {

                        Notification.show(
                              App.translate(AbstractListController.class, "notSingleSelected"),
                              Notification.Type.WARNING_MESSAGE
                        );

                     } else {
                        new PasswordChangePopup(newPassword -> passwordChangeService.changePassword(
                              selection.iterator().next().getId(),
                              newPassword
                        ));
                     }
                  })
                  .withStyleName("redicon")
                  .withIcon(VaadinIcons.PASSWORD)
      );
      return Optional.of(controls);
   }

   @Override
   protected EntityRepository<SecurityUser, Long, SecurityUserFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<SecurityUser> getEntityClass() {
      return SecurityUser.class;
   }

   @Override
   protected AbstractFormController<SecurityUser, Long> getFormController() {
      return new AbstractFormController<SecurityUser, Long>(SecurityUser.class) {
         @Override
         protected EntityRepository<SecurityUser, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<SecurityUser> createFieldLayout(SecurityUser model) {
            return new SecurityUserForm(model.getId() != null);
         }
      };
   }

   @Override
   public String getViewName() {
      return "users-system";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.USER_STAR;
   }

   @Override
   public String getViewRole() {
      return "USERS_SYSTEM";
   }

   @Override
   public String getViewGroupName() {
      return "system";
   }
}
