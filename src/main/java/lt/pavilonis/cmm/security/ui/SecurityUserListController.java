package lt.pavilonis.cmm.security.ui;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.security.service.SecurityUserRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.converter.BooleanValueProviderAdapter;
import lt.pavilonis.cmm.common.converter.CollectionValueProviderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class SecurityUserListController extends AbstractListController<SecurityUser, Long, SecurityUserFilter> {

   @Autowired
   private SecurityUserRepository repository;

   @Override
   protected ListGrid<SecurityUser> createGrid() {

      return new ListGrid<SecurityUser>(SecurityUser.class) {

         @Override
         protected List<String> columnOrder() {
            return Arrays.asList("username", "name", "email", "enabled", "authorities");
         }

         @Override
         protected Map<String, ValueProvider<SecurityUser, ?>> getCustomColumns() {
            return ImmutableMap.of(
                  "authorities", new CollectionValueProviderAdapter<>(SecurityUser::getAuthorities),
                  "enabled", new BooleanValueProviderAdapter<>(SecurityUser::getEnabled)
            );
         }
      };
   }

   @Override
   protected FilterPanel<SecurityUserFilter> createFilterPanel() {
      return new SecurityUserFilterPanel();
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
         protected FieldLayout<SecurityUser> createFieldLayout() {
            return new SecurityUserForm();
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
