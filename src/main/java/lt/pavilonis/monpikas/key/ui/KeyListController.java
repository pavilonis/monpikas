package lt.pavilonis.monpikas.key.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import lt.pavilonis.monpikas.key.Key;
import lt.pavilonis.monpikas.key.KeyListRepository;
import lt.pavilonis.monpikas.scanner.ScannerRepository;
import lt.pavilonis.monpikas.user.PresenceTimeRepository;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.user.UserFormController;
import lt.pavilonis.monpikas.user.UserListRepository;
import lt.pavilonis.monpikas.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@UIScope
@SpringComponent
public class KeyListController extends AbstractListController<Key, Integer, KeyListFilter> {

   private final KeyListRepository repository;
   private final ScannerRepository scannerRepository;
   private final UserListRepository userListRepository;
   private final PresenceTimeRepository presenceTimeRepository;
   private final String supervisorRole;
   private final UserRepository userRepository;

   public KeyListController(KeyListRepository repository, ScannerRepository scannerRepository,
                            UserListRepository userListRepository, PresenceTimeRepository presenceTimeRepository,
                            @Value("${user.supervisor.organizationRole}") String supervisorRole,
                            UserRepository userRepository) {
      this.repository = repository;
      this.scannerRepository = scannerRepository;
      this.userListRepository = userListRepository;
      this.presenceTimeRepository = presenceTimeRepository;
      this.supervisorRole = supervisorRole;
      this.userRepository = userRepository;
   }

   @Override
   protected void addGridClickListener(ListGrid<Key> table) {
      table.addItemClickListener(click -> {
         if (click.getMouseEventDetails().isDoubleClick()) {
            String cardCode = click.getItem()
                  .getUser()
                  .getCardCode();

            User selectedUser = userRepository.load(cardCode, true);
            createUserController().edit(selectedUser, null, false);
         }
      });
   }

   private UserFormController createUserController() {
      return new UserFormController(userListRepository, presenceTimeRepository, supervisorRole, userRepository) {
         @Override
         protected void updateGridOnSaveUpdate(User itemToEdit, ListGrid<User> listGrid, User updatedItem) {
            /*do nothing*/
         }
      };
   }

   @Override
   protected ListGrid<Key> createGrid() {
      return new KeyListGrid();
   }

   @Override
   protected FilterPanel<KeyListFilter> createFilterPanel() {
      return new KeyListFilterPanel(scannerRepository.load());
   }

   @Override
   protected EntityRepository<Key, Integer, KeyListFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<Key> getEntityClass() {
      return Key.class;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
   }

   @Override
   public String getViewName() {
      return "keys";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.KEY;
   }

   @Override
   public String getViewRole() {
      return "KEYS";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
