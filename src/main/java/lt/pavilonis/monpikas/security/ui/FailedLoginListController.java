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
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodTextFilter;
import lt.pavilonis.monpikas.common.ui.filter.PeriodTextFilterPanel;
import lt.pavilonis.monpikas.security.FailedLogin;
import lt.pavilonis.monpikas.security.FailedLoginRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@SpringComponent
@UIScope
public class FailedLoginListController extends AbstractListController<FailedLogin, Long, IdPeriodTextFilter> {

   private final FailedLoginRepository repository;

   @Override
   protected ListGrid<FailedLogin> createGrid() {
      return new ListGrid<>(FailedLogin.class) {
         @Override
         protected List<String> columnOrder() {
            return List.of("id", "created", "name", "address");
         }
      };
   }

   @Override
   protected FilterPanel<IdPeriodTextFilter> createFilterPanel() {
      return new PeriodTextFilterPanel() {
         @Override
         protected void setDefaultValues() {
            getPeriodStart().setValue(LocalDate.now().minusDays(1));
         }
      };
   }

   @Override
   protected EntityRepository<FailedLogin, Long, IdPeriodTextFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
   }

   @Override
   protected Class<FailedLogin> getEntityClass() {
      return FailedLogin.class;
   }

   @Override
   public String getViewName() {
      return "failed-logins";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.BAN;
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
