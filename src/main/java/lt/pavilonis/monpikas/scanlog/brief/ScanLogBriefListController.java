package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.monpikas.scanlog.ScannerRepository;
import lt.pavilonis.monpikas.user.UserRepository;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;

import java.util.Optional;

@UIScope
@SpringComponent
public class ScanLogBriefListController extends AbstractListController<ScanLogBrief, Void, ScanLogBriefFilter> {

   private final ScanLogBriefRepository repository;
   private final ScannerRepository scannerRepository;
   private final UserRepository userRepository;

   public ScanLogBriefListController(ScanLogBriefRepository repository, ScannerRepository scannerRepository,
                                     UserRepository userRepository) {
      this.repository = repository;
      this.scannerRepository = scannerRepository;
      this.userRepository = userRepository;
   }

   @Override
   protected ListGrid<ScanLogBrief> createGrid() {
      return new ScanLogBriefListGrid();
   }

   @Override
   protected FilterPanel<ScanLogBriefFilter> createFilterPanel() {
      return new ScanLogBriefFilterPanel(
            userRepository.loadRoles(),
            scannerRepository.loadAll()
      );
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
