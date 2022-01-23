package lt.pavilonis.monpikas.security.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import lt.pavilonis.monpikas.security.LoginEvent;
import lt.pavilonis.monpikas.security.LoginEventFilter;
import lt.pavilonis.monpikas.security.LoginEventRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@SpringComponent
@UIScope
public class LoginEventListController extends AbstractListController<LoginEvent, Long, LoginEventFilter> {

   private final LoginEventRepository repository;

   @Override
   protected ListGrid<LoginEvent> createGrid() {
      return new ListGrid<>(LoginEvent.class) {
         @Override
         protected List<String> columnOrder() {
            return List.of("id", "created", "name", "address", "success", "logout");
         }

         @Override
         protected List<String> columnsToCollapse() {
            return List.of("id", "logout");
         }
      };
   }

   @Override
   protected FilterPanel<LoginEventFilter> createFilterPanel() {
      return new LoginEventFilterPanel();
   }

   @Override
   protected EntityRepository<LoginEvent, Long, LoginEventFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
   }

   @Override
   protected Class<LoginEvent> getEntityClass() {
      return LoginEvent.class;
   }

   @Override
   public String getViewName() {
      return "login-events";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.SIGN_IN;
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
