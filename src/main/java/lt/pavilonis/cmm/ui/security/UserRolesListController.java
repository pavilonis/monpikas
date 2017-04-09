package lt.pavilonis.cmm.ui.security;

import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.canteen.service.SecurityUserDetailsService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.converter.CollectionValueProviderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class UserRolesListController extends AbstractListController<SecurityUser, Long, SecurityUserFilter> {

   @Autowired
   private SecurityUserDetailsService service;

   @Override
   protected ListGrid<SecurityUser> createGrid() {
      return new ListGrid<SecurityUser>(SecurityUser.class) {
         @Override
         protected List<String> getProperties(Class<SecurityUser> type) {
            return Arrays.asList("username", "name", "email", "enabled");
         }

         @Override
         protected Map<String, ValueProvider<SecurityUser, ?>> getCustomColumns() {
            return Collections.singletonMap(
                  "authorities",
                  new CollectionValueProviderAdapter<>(SecurityUser::getAuthorities)
            );
         }

         @Override
         protected void customize() {
//            setConverter("authorities", new CollectionValueProviderAdapter());
//            setConverter("enabled", new BooleanCellConverter());
         }
      };
   }

   @Override
   protected FilterPanel<SecurityUserFilter> createFilterPanel() {
      return new SecurityUserFilterPanel();
   }

   @Override
   protected EntityRepository<SecurityUser, Long, SecurityUserFilter> getEntityRepository() {
      return service;
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
            return service;
         }

         @Override
         protected FieldLayout<SecurityUser> createFieldLayout() {
            return new SecurityUserFormView();
         }
      };
   }
}
