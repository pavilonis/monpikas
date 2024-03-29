package lt.pavilonis.monpikas.security.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import lombok.AllArgsConstructor;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.AbstractFormController;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.ControlPanel;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.FieldLayout;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.field.AButton;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import lt.pavilonis.monpikas.security.SystemUser;
import lt.pavilonis.monpikas.security.SystemUserRepository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@SpringComponent
@UIScope
public class SystemUserListController extends AbstractListController<SystemUser, Long, SystemUserFilter> {

   private final SystemUserRepository repository;

   @Override
   protected ListGrid<SystemUser> createGrid() {
      return new ListGrid<>(SystemUser.class) {
         @Override
         protected List<String> columnOrder() {
            return List.of("id", "username", "name", "email", "enabled", "authorities");
         }
      };
   }

   @Override
   protected FilterPanel<SystemUserFilter> createFilterPanel() {
      return new SystemUserFilterPanel();
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      var controls = new ControlPanel(click -> actionCreate(), click -> actionDelete());
      var button = new AButton(getClass(), "passwordReset")
            .withClickListener(click -> {
               @SuppressWarnings("unchecked")
               ListGrid<SystemUser> grid = (ListGrid<SystemUser>) mainArea;
               Set<SystemUser> selection = grid.getSelectedItems();

               if (CollectionUtils.isEmpty(selection)
                     || selection.size() > 1) {

                  Notification.show(
                        App.translate(AbstractListController.class, "notSingleSelected"),
                        Notification.Type.WARNING_MESSAGE
                  );

               } else {
                  new PasswordChangePopup(newPassword ->
                        repository.changePassword(selection.iterator().next().getId(), newPassword));
               }
            })
            .withStyleName("redicon")
            .withIcon(VaadinIcons.PASSWORD);

      controls.addComponent(button);
      return Optional.of(controls);
   }

   @Override
   protected EntityRepository<SystemUser, Long, SystemUserFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<SystemUser> getEntityClass() {
      return SystemUser.class;
   }

   @Override
   protected AbstractFormController<SystemUser, Long> getFormController() {
      return new AbstractFormController<SystemUser, Long>(SystemUser.class) {
         @Override
         protected EntityRepository<SystemUser, Long, ?> getEntityRepository() {
            return repository;
         }

         @Override
         protected FieldLayout<SystemUser> createFieldLayout(SystemUser model) {
            return new SystemUserForm(model.getId() != null);
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
