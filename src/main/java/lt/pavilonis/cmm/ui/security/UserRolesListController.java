package lt.pavilonis.cmm.ui.security;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.canteen.service.SecurityUserDetailsService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.BooleanCellConverter;
import lt.pavilonis.cmm.converter.CollectionCellConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserRolesListController extends AbstractListController<SecurityUser, String, SecurityUserFilter> {

   @Autowired
   private SecurityUserDetailsService service;

   @Override
   protected ListTable<SecurityUser> createTable() {
      return new ListTable<SecurityUser>(SecurityUser.class) {
         @Override
         protected List<String> getProperties(Class<SecurityUser> type) {
            return Arrays.asList("username", "name", "email", "authorities", "enabled");
         }

         @Override
         protected void customize(MessageSourceAdapter messageSource) {
            setConverter("authorities", new CollectionCellConverter());
            setConverter("enabled", new BooleanCellConverter());
         }
      };
   }

   @Override
   protected FilterPanel<SecurityUserFilter> createFilterPanel() {
      return new SecurityUserFilterPanel();
   }

   @Override
   protected EntityRepository<SecurityUser, String, SecurityUserFilter> getEntityRepository() {
      return service;
   }

   @Override
   protected Class<SecurityUser> getEntityClass() {
      return SecurityUser.class;
   }

   @Override
   protected AbstractFormController<SecurityUser, String> getFormController() {
      return new AbstractFormController<SecurityUser, String>() {
         @Override
         protected EntityRepository<SecurityUser, String, ?> getEntityRepository() {
            return service;
         }

         @Override
         protected FormView<SecurityUser> createFormView() {
            return new SecurityUserFormView();
         }

         @Override
         protected MessageSourceAdapter getMessageSource() {
            return UserRolesListController.this.messageSource;
         }


      };
   }
}
