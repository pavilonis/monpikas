package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
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
public class ScanLogBriefListController extends AbstractListController<ScanLogBrief, Void, ScanLogBriefFilter> {

   private final ScanLogBriefRepository repository;
   private final ScannerRepository scannerRepository;
   private final UserRepository userRepository;
   private final UserListRepository userListRepository;
   private final PresenceTimeRepository presenceTimeRepository;
   private final String supervisorRole;

   public ScanLogBriefListController(ScanLogBriefRepository repository, ScannerRepository scannerRepository,
                                     UserRepository userRepository, UserListRepository userListRepository,
                                     PresenceTimeRepository presenceTimeRepository,
                                     @Value("${user.supervisor.organizationRole}") String supervisorRole) {
      this.repository = repository;
      this.scannerRepository = scannerRepository;
      this.userRepository = userRepository;
      this.userListRepository = userListRepository;
      this.presenceTimeRepository = presenceTimeRepository;
      this.supervisorRole = supervisorRole;
   }

   @Override
   protected ListGrid<ScanLogBrief> createGrid() {
      return new ScanLogBriefListGrid();
   }

   @Override
   protected FilterPanel<ScanLogBriefFilter> createFilterPanel() {
      return new ScanLogBriefFilterPanel(
            userRepository.loadRoles(),
            scannerRepository.load()
      );
   }

   @Override
   protected void addGridClickListener(ListGrid<ScanLogBrief> table) {
      table.addItemClickListener(click -> {
         if (click.getMouseEventDetails().isDoubleClick()) {
            ScanLogBrief item = click.getItem();
            User selectedUser = userRepository.load(item.getCardCode(), true);
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
   protected EntityRepository<ScanLogBrief, Void, ScanLogBriefFilter> getEntityRepository() {
      return repository;
   }

   @Override
   protected Class<ScanLogBrief> getEntityClass() {
      return ScanLogBrief.class;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
   }

   @Override
   public String getViewName() {
      return "scanlog";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.BARCODE;
   }

   @Override
   public String getViewRole() {
      return "SCANLOG";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
